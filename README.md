# Business application setup

## Docker

---

### Step 1: Install docker

### Step 2: open a terminal in the business directory

### Step 3: In the terminal window type:

``docker-compose build && docker-compose up``

### Step 4: To view your application visit http://localhost:8080 after its fully up, you can also go to http://localhost:8082 to connect to the database using the credentials root and password business.

---

## XAMPP

---

### Step 1: install xampp and open apache and mysql ports, go to the http://localhost/phpmyadmin

### Step 2: Create database BusinessDb the tables will be created automatically, after that create user with your own credentials

### Step 3: Update the credentials in the application.properties, everything should work just fine

### Step 4: Open terminal and run these commands

``npm install && npx webpack``

---

## USAGE

---

#### This application has two controllers a @RestController and a @Controller, to get started you can visit the localhost:8080 and you will be redirected to the thymeleaf templates where you can interact with the application in the enviorment I built.

#### If you wish to control the api using postman or maybe you wish to create your own interface the endpoints are:

## GET

#### http://localhost:8080/api/v1/business/main

* Required params are: ``businessName|type=string`` and ``businessEmail|type=string``
* Response Type: JSON
* Response Example: ``{"businessId":1,"businessName":"business1","businessEmail":"business1@gmail.com","businessPhone":"0000","latitude":41.0,"longitude":51.0}``
* Response Types: STRING, STRING, STRING, DOUBLE

### http://localhost:8080/api/v1/business/schedules

* Required params are: ``businessName|type=string`` and ``businessEmail|type=string``
* Response Type: JSON
* Response Example: ``[{"hoursId":1,"dayOfWeek":0,"openingHours":"10:00:00","closingHours":"19:00:00"}]``
* Response Types: INT, INT, STRING, STRING

### http://localhost:8080/api/v1/business/holidays

* Required params are: ``businessName|type=string`` and ``businessEmail|type=string``
* Response Type: JSON
* Response Example: ``[{"holidaysId":1,"calendarHolidayStart":"2023-01-01","calendarHolidayEnd":"2023-01-07"}]``
* Response Types: INT, STRING, STRING

---
