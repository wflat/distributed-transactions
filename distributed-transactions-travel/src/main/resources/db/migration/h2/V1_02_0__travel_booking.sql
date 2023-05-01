create table travel__booking
(
    id                  varchar(255) not null,
    status              varchar(255) not null,
    status_details      varchar(255),
    created             timestamp,
    updated             timestamp,
    process_instance_id varchar(255),
    hotel_id            varchar(255),
    hotel_quantity      integer,
    hotel_approved      boolean default false,
    car_id              varchar(255),
    car_quantity        integer,
    car_approved        boolean default false,
    flight_id           varchar(255),
    flight_quantity     integer,
    flight_approved     boolean default false,
    version             integer      not null,
    constraint travel__booking_pk primary key (id)
);
