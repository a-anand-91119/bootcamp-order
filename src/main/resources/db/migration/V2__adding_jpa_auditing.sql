alter table users
    add column created_date       timestamp   not null default current_timestamp,
    add column created_by         varchar(50) not null default 'system',
    add column last_modified_date timestamp   null,
    add column last_modified_by   varchar(50) null,
    add column is_deleted         boolean     not null default false;
