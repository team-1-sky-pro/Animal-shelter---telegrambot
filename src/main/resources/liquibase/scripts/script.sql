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

-- changeSet x3imal:29
ALTER TABLE adoptions
    RENAME COLUMN isactive TO is_active;

