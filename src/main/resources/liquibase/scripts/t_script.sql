-- liquibase formatted sql

-- changeSet annabelousova:41
INSERT INTO adoptions(
	id, adoption_date, trial_end_date, is_active, user_id, pet_id)
	VALUES (1, null, null, false, 177212830, 1);
INSERT INTO shelters(
	id, shelter_type, contacts, security_contacts, working_hours)
	VALUES (1, 'cat', 'г. Астана, ул. Спасская, 8-800-888-88-88', '8(999)999-99-99', 'ежедневно, 09:00–21:00');
ALTER TABLE reports
    ADD is_approved BOOLEAN;


-- changeSet x3imal:50
ALTER TABLE users
ALTER COLUMN phone TYPE VARCHAR(50);
ALTER TABLE users
DROP CONSTRAINT ch_phone;

-- changeSet x3imal:50.1
ALTER TABLE pets
    ADD CONSTRAINT fk_pet_photo
        FOREIGN KEY (photo_id) REFERENCES photos(id);
ALTER TABLE pets ALTER COLUMN photo_id DROP NOT NULL;
ALTER TABLE photos
    ALTER COLUMN report_id DROP NOT NULL;


-- changeSet x3imal:50.2
INSERT INTO pets (pet_name, birthday, description, is_adopted, shelter_id, photo_id)
VALUES
    ('Барсик', '2020-01-01', 'Дружелюбный оранжевый кот', false, 1, null),
    ('Мурка', '2019-05-15', 'Спокойная черная кошка', false, 1, null),
    ('Рекс', '2018-10-10', 'Энергичный пёс породы лабрадор', false, 1, null),
    ('Луна', '2021-03-23', 'Молодая сибирская кошка', false, 1, null);




