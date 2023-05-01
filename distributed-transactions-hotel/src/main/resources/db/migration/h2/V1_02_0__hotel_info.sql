create table hotel__info
(
    id          varchar(255)  not null,
    description varchar(2000) not null,
    created     timestamp,
    updated     timestamp,
    quantity    integer,
    version     integer       not null,
    constraint hotel__info_pk primary key (id)
);
