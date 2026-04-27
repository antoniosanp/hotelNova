# HotelNova - Hotel Management System (Java SE + JDBC + JOptionPane)

## Overview
HotelNova is a Java SE internal application for managing rooms, guests, users, and reservations, built with layered architecture (`controller`, `service`, `dao`, `model`) and JDBC persistence with PostgreSQL.

It includes:
- Role-based authentication (`ADMIN` / `RECEPTIONIST`).
- Modal menus with `JOptionPane`.
- CRUD for `Room`, `Guest`, `User`, and `Reservation`.
- Business validations (availability, non-overlapping reservations, active guest, valid dates).
- JDBC transactions for `check-in` and `check-out`.
- CSV export and logging in `app.log`.

## Prerequisites
- Java 17+ (Java 21 recommended).
- Maven 3.9+.
- PostgreSQL.
- SQL schema applied from `src/main/resources/schema.sql`.

## Configuration
1. Clone the repository.
2. Configure database credentials in:
   - `src/main/resources/database.properties`
3. Configure app parameters in:
   - `src/main/resources/config.properties`

Key parameters:
- `iva=0.19`
- `horaCheckIn=15`
- `horaCheckOut=12`

## Run
1. Compile:
```bash
mvn clean compile
```
2. Start the app:
```bash
mvn exec:java -Dexec.mainClass="Main"
```

## Tests
Run:
```bash
mvn test
```

Included tests:
- `AuthControllerTest` (login).
- `ReservationControllerCrudTest` (reservation CRUD flow).

## Menu Flow
- Main menu:
  - Login
  - User registration
- If logged user has role `ADMIN`, access to:
  - `GuestMenu`
  - `RoomMenu`
  - `ReservationMenu`
  - `ExportMenu`

## Core Features
- `Room`:
  - Fields: `room_type`, `room_number`, `room_capacity`, `room_price`, `room_state`, `isActive`.
  - Special filters: `findByType`, `findByState`.
- `Guest`:
  - Fields: `name`, `email`, `isActive`.
  - Special lookup: `findByEmail`.
  - Special filter: active guests only.
- `Reservation`:
  - Transactional `check-in`: insert reservation + set room to `OCUPADA`.
  - Transactional `check-out`: close reservation + calculate total with VAT + set room to `DISPONIBLE`.
  - No-overlap validation.

## Exports
- `habitaciones_export.csv`
- `reservas_activas.csv`

## Logs
- File: `app.log`
- Records HTTP-like traces (`GET`, `POST`, `PATCH`, `DELETE`) and errors.

## Screenshots (JOptionPane)
Add screenshots in a folder, for example:
- `docs/screenshots/login.png`
- `docs/screenshots/admin-menu.png`
- `docs/screenshots/room-menu.png`

## Diagrams
Add in `docs/diagrams`:
- Class diagram.
- ERD.

## Coder Information
- Name: Antonio Santiago Pulgarín Arango
- Clan: Hamilton
- Email: antonio.pulgarin97@gmail.com
- Github: antoniosanp
