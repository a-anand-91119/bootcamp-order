create table if not exists users
(
    id         uuid primary key default gen_random_uuid(),
    first_name varchar(255),
    last_name  varchar(255),
    email      varchar(255)
);

