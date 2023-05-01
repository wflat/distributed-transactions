# Workshop Distributed Transactions

## Install

`mvn clean install`

## Services

### ActiveMQ

#### App

* GET http://localhost:8120/actuator/hawtio

#### Broker

* TCP: tcp://localhost:61616

### Camunda

#### App

User: admin
Pswd: admin

* GET http://127.0.0.1:8110/camunda/app/welcome/default/#!/login

#### DB

User: sa
Pswd: sa

* TCP: jdbc:h2:tcp://localhost:9110/mem:camunda
* HTTP: http://localhost:8110/h2-console - URL `jdbc:h2:mem:camunda;DB_CLOSE_ON_EXIT=FALSE`

### Travel Service

#### REST API

* get all: GET http://localhost:8100/travel-bookings
* get by id: GET http://localhost:8100/travel-bookings/TRAVEL-BOOKING-1
* add one: POST http://localhost:8100/travel-bookings with body `{
  "hotelId": "HOTEL-INFO-1",
  "hotelQuantity": 2,
  "carId": "CAR-INFO-3",
  "carQuantity": 1,
  "flightId": "FLIGHT-INFO-1",
  "flightQuantity": 5
  }`

#### DB

User: sa
Pswd: sa

* TCP: jdbc:h2:tcp://localhost:9100/mem:travelservice
* HTTP: http://localhost:8100/h2-console - URL `jdbc:h2:mem:travelservice;DB_CLOSE_ON_EXIT=FALSE`

### CAR Service

#### REST API

* get all: GET http://localhost:8140/car-infos
* get by id: GET http://localhost:8140/car-infos/CAR-INFO-1 
* patch by id: PATCH http://localhost:8140/car-infos/CAR-INFO-1 with body `{
  "description": "Q3",
  "quantity": 2
  }`
* patch quantity: PATCH http://localhost:8140/car-infos/CAR-INFO-2/quantity with body `{
  "travelBookingId": "TRAVEL-1",
  "eventType": "BOOK",
  "entityId": "CAR-INFO-2",
  "entityQuantity": 8
  }`
* add one: POST http://localhost:8140/car-infos with body `{
  "description": "VW Golf",
  "quantity": 8
  }`

#### DB

User: sa
Pswd: sa

* TCP: jdbc:h2:tcp://localhost:9140/mem:carservice
* HTTP: http://localhost:8140/h2-console - URL `jdbc:h2:mem:carservice;DB_CLOSE_ON_EXIT=FALSE`

### FLIGHT Service

#### REST API

* get all: GET http://localhost:8150/flight-infod
* get by id: GET http://localhost:8150/flight-infos/FLIGHT-INFO-1
* patch by id: PATCH http://localhost:8150/flight-infos/FLIGHT-INFO-1 with body `{
  "description": "München => Nürnberg",
  "quantity": 2
  }`
* patch quantity: PATCH http://localhost:8150/flight-infos/FLIGHT-INFO-2/quantity with body `{
  "travelBookingId": "TRAVEL-1",
  "eventType": "BOOK",
  "entityId": "FLIGHT-INFO-2",
  "entityQuantity": 8
  }`
* add one: POST http://localhost:8150/flight-infos with body `{
  "description": "München => Nürnberg",
  "quantity": 8
  }`

#### DB

User: sa
Pswd: sa

* TCP: jdbc:h2:tcp://localhost:9150/mem:flightservice
* HTTP: http://localhost:8150/h2-console - URL `jdbc:h2:mem:flightservice;DB_CLOSE_ON_EXIT=FALSE`

### HOTEL Service

#### REST API

* get all: GET http://localhost:8130/hotel-infos
* get by id: GET http://localhost:8130/hotel-infos/HOTEL-INFO-1
* patch by id: PATCH http://localhost:8130/hotel-infos/HOTEL-INFO-1 with body `{
  "description": "description": "Leonardo Royal Hotel Nürnberg",
  "quantity": 2
  }`
* patch quantity: PATCH http://localhost:8130/hotel-infos/HOTEL-INFO-2/quantity with body `{
  "travelBookingId": "TRAVEL-1",
  "eventType": "BOOK",
  "entityId": "HOTEL-INFO-2",
  "entityQuantity": 8
  }`
* add one: POST http://localhost:8130/hotel-infos with body `{
  "description": "Leonardo Royal Hotel Nürnberg",
  "quantity": 8
  }`

#### DB

User: sa
Pswd: sa

* TCP: jdbc:h2:tcp://localhost:9130/mem:hotelservice
* HTTP: http://localhost:8130/h2-console - URL `jdbc:h2:mem:hotelservice;DB_CLOSE_ON_EXIT=FALSE`
