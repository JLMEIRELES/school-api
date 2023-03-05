package com.example.school.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.school.annotation.ProtectedEndpoint;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.records.exceptions.ExceptionData;
import com.example.school.service.JWTService;
import com.example.school.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recoverToken(request);
        if(tokenJWT != null){
            try {
                var subject = jwtService.getSubject(tokenJWT);
                var user = userService.findByEmail(subject);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                HandlerExecutionChain handlerExecutionChain = Objects.requireNonNull(requestMappingHandlerMapping.getHandler(request), "Handler not found for request " + request.getRequestURI());
                HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
                if (AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ProtectedEndpoint.class) != null) {
                    ProtectedEndpoint annotation = handlerMethod.getMethodAnnotation(ProtectedEndpoint.class);
                    assert annotation != null;
                    UserType[] allowedRules = annotation.allowedUserTypes();
                    User principal = (User) authentication.getPrincipal();
                    var selfUpdate = annotation.selfUpdate();
                    String[] uris = request.getRequestURI().split("/");
                    var change = false;
                    if(selfUpdate){
                        change = canBeChanged(principal, uris);
                    }
                    if (!Arrays.asList(allowedRules).contains(principal.getUserType()) && !change){
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        ExceptionData exceptionData = new ExceptionData("User do not have the necessary roles");
                        response.getWriter().write(convertObjectToJson(exceptionData));
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            } catch (JWTCreationException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                ExceptionData exceptionData = new ExceptionData("Error on generate JWT");
                response.getWriter().write(convertObjectToJson(exceptionData));
            } catch (JWTVerificationException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                ExceptionData exceptionData = new ExceptionData("Invalid or Expired JWT");
                response.getWriter().write(convertObjectToJson(exceptionData));
            } catch (Exception e) {
                throw new ServletException("Error processing request", e);
            }
        }
        else{
            String requestURI = request.getRequestURI();
            String[] uriParts = requestURI.split("/");
            if (!Arrays.asList(uriParts).contains("login")){
                ExceptionData exceptionData = new ExceptionData("No JWT was passed");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(convertObjectToJson(exceptionData));
                return;
            }
            filterChain.doFilter(request, response);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private boolean canBeChanged(User user, String[] uris){
        return Arrays.asList(uris).contains(user.getId().toString());
    }
}
