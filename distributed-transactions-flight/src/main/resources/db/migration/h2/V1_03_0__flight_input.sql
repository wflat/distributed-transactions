create table flight__input
(
    id         varchar(255) not null,
    processed  boolean default false,
    created    timestamp,
    updated    timestamp,
    event_json varchar(2000),
    version    integer      not null,
    constraint flight__input_pk primary key (id)
);
