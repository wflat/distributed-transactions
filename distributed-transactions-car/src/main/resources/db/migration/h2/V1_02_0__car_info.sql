create table car__info
(
    id          varchar(255)  not null,
    description varchar(2000) not null,
    created     timestamp,
    updated     timestamp,
    quantity    integer,
    version     integer       not null,
    constraint car__info_pk primary key (id)
);
