-- liquibase formatted sql

-- changeSet x3imal:1
create TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       user_name VARCHAR(50),
                       email VARCHAR(50),
                       phone VARCHAR(10),
                       is_volunteer BOOLEAN
);

create TABLE pets (
                      id BIGSERIAL PRIMARY KEY,
                      pet_name VARCHAR(50),
                      birthday DATE,
                      description VARCHAR(250),
                      is_adopted BOOLEAN,
                      shelter_id BIGSERIAL,
                      photo_id BIGSERIAL
);

create TABLE adoptions (
                           id BIGSERIAL PRIMARY KEY,
                           adoption_date TIMESTAMP,
                           trial_end_date TIMESTAMP,
                           is_active BOOLEAN,
                           user_id BIGSERIAL,
                           pet_id BIGSERIAL
);

create TABLE shelters (
                          id BIGSERIAL PRIMARY KEY,
                          shelter_type VARCHAR(50),
                          contacts VARCHAR(250),
                          security_contacts VARCHAR(100),
                          working_hours VARCHAR(100)
);

create TABLE reports (
                         id BIGSERIAL PRIMARY KEY,
                         report_date_time TIMESTAMP,
                         report_text VARCHAR(250),
                         is_approved BOOLEAN
);

create TABLE photos (
                        id BIGSERIAL PRIMARY KEY,
                        file_path VARCHAR(150),
                        is_initial BOOLEAN,
                        report_id BIGSERIAL
);
