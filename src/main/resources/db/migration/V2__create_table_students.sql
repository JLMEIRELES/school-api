create table students(
    registration varchar(5),
    user_id integer REFERENCES users (id)
);