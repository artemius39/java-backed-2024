create table link (
    id bigserial primary key,
    url text unique not null,
    last_updated timestamp with time zone not null
);
