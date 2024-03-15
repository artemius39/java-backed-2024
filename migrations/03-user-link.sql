create table user_link (
    user_id bigint references "user"(id) not null,
    link_id bigint references link(id) not null
);
