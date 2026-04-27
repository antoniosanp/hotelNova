CREATE TABLE IF NOT EXISTS users (
                                     id_user SERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL
    );



CREATE TABLE IF NOT EXISTS room (
                                    id_room SERIAL PRIMARY KEY,
                                    room_type VARCHAR(255) not null,
    room_number INTEGER NOT NULL UNIQUE,
    room_capacity INTEGER NOT NULL,
    room_price DECIMAL(10,2) NOT NULL,
    room_state VARCHAR(50) NOT NULL,
    isActive BOOLEAN NOT NULL DEFAULT TRUE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );



CREATE TABLE IF NOT EXISTS guest (
                                     id_guest SERIAL PRIMARY KEY,
                                     isActive BOOLEAN not null default TRUE,
                                     name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
    );



CREATE EXTENSION IF NOT EXISTS btree_gist;



CREATE TABLE IF NOT EXISTS reservation (
                                           id_reservation SERIAL PRIMARY KEY,

                                           id_room INTEGER NOT NULL REFERENCES room(id_room)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,

    id_guest INTEGER NOT NULL REFERENCES guest(id_guest)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,

    total_nights INTEGER NOT NULL,

    day_in DATE NOT NULL,
    day_out DATE NOT NULL,

    check_in BOOLEAN NOT NULL DEFAULT FALSE,
    check_out BOOLEAN NOT NULL DEFAULT FALSE,

    CHECK (day_in < day_out),

    EXCLUDE USING gist (
                           id_room WITH =,
                           daterange(day_in, day_out, '[)') WITH &&
        )
    );