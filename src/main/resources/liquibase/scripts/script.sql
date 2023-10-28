-- liquibase formatted sql

-- changeSet annabelousova:1
create TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(10) NOT NULL UNIQUE,
    isVolunteer BOOLEAN,
    CONSTRAINT ch_phone CHECK(phone like '[0-9]*10')
);
create TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    pet_name VARCHAR(50) NOT NULL UNIQUE,
    birthday TIMESTAMP NOT NULL,
    description VARCHAR(250),
    isAdopted BOOLEAN,
    shelter_id BIGSERIAL,
    photo_id BIGSERIAL
);
create TABLE adoptions (
    id BIGSERIAL PRIMARY KEY,
    adoption_date TIMESTAMP NOT NULL,
    trial_end_date TIMESTAMP NOT NULL,
    isActive BOOLEAN NOT NULL,
    user_id BIGSERIAL,
    pet_id BIGSERIAL
);
create TABLE shelters (
    id BIGSERIAL PRIMARY KEY,
    shelter_type VARCHAR(50) NOT NULL UNIQUE,
    contacts VARCHAR(250) NOT NULL,
    security_contacts VARCHAR(100) NOT NULL,
    working_hours VARCHAR(100) NOT NULL
);
create TABLE reports (
    id BIGSERIAL PRIMARY KEY,
    report_datetime TIMESTAMP,
    report_text VARCHAR(250)
);
create TABLE photos (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(150),
    isInitial BOOLEAN,
    report_id BIGSERIAL
);

-- changeSet SyutinS:35
ALTER TABLE users
    RENAME COLUMN isVolunteer TO is_volunteer;

-- changeSet x3imal:29
ALTER TABLE adoptions
    RENAME COLUMN isactive TO is_active;

-- changeSet x3imal:29.1
ALTER TABLE photos
    RENAME COLUMN isinitial TO is_initial;

-- changeSet x3imal:32
ALTER TABLE reports
RENAME COLUMN report_datetime TO report_date_time;

-- changeSet Rnd-mi:30
ALTER TABLE pets
RENAME COLUMN isAdopted TO is_adopted;

-- changeSet Rnd-mi:30.1
ALTER TABLE pets
ALTER COLUMN birthday TYPE DATE;

-- changeset annabelousova:38
alter table users
alter column email drop not null;
alter table users
alter column phone drop not null;
alter table pets
alter column birthday drop not null;
alter table adoptions
alter column adoption_date drop not null;
alter table adoptions
alter column trial_end_date drop not null;
