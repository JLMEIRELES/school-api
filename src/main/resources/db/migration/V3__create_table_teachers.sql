create table teachers(
    code varchar(5),
    academic_background varchar(200),
    user_id integer REFERENCES users (id)
);