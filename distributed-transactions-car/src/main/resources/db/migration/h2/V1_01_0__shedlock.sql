CREATE TABLE CAR__SHEDLOCK
(
    name       VARCHAR2(128),
    lock_until TIMESTAMP,
    locked_at  TIMESTAMP,
    locked_by  VARCHAR2(255),
    constraint CAR__SHEDLOCK_PK primary key (name)
);
