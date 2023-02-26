create table users(

    id serial,
    name varchar(100) not null,
    email varchar(100) not null unique,
    cpf varchar(11) not null unique,
    password varchar(255) not null,
    born_date date not null,
    user_type varchar(100) not null,

    primary key(id)

);