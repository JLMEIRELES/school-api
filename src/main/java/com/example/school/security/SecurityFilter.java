package com.example.school.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recoverToken(request);
        if(tokenJWT != null){
            try {
                var subject = jwtService.getSubject(tokenJWT);
                var user = userService.findByEmail(subject);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String requestURI = request.getRequestURI();
                String[] uriParts = requestURI.split("/");
                String[] createStatements = {"create-student", "create,administrator", "create-teacher"};
                if (Arrays.stream(uriParts).anyMatch(part -> List.of(createStatements).contains(part))) {
                    User principal = (User) authentication.getPrincipal();
                    if (principal.getUserType() != UserType.ADM){
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        ExceptionData exceptionData = new ExceptionData("User do not have the necessary roles");
                        response.getWriter().write(convertObjectToJson(exceptionData));
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            } catch(RuntimeException e){
                ExceptionData exceptionData;
                if (e instanceof JWTCreationException ){
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                     exceptionData = new ExceptionData("Error on generate JWT");
                }
                else if(e instanceof JWTVerificationException){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    exceptionData = new ExceptionData("Invalid or Expired JWT");
                }
                else{
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    exceptionData = new ExceptionData(e.getMessage());
                }
                response.getWriter().write(convertObjectToJson(exceptionData));
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
}
