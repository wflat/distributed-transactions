create table travel__input
(
    id          varchar(255) not null,
    entity_type varchar(255) not null,
    processed   boolean default false,
    created     timestamp,
    updated     timestamp,
    event_json  varchar(2000),
    version     integer      not null,
    constraint travel__input_pk primary key (id)
);
