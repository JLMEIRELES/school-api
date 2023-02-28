create table administrators(
    code varchar(5),
    "position" varchar(200),
    user_id integer REFERENCES users (id)
);