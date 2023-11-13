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


-- changeSet x3imal:50.2
INSERT INTO pets (pet_name, birthday, description, is_adopted, shelter_id, photo_id)
VALUES
    ('Барсик', '2020-01-01', 'Дружелюбный оранжевый кот.', false, 1, null),
    ('Мурка', '2019-05-15', 'Спокойная черная кошка.', false, 1, null),
    ('Рекс', '2018-10-10', 'Сиамский котенок.', false, 1, null),
    ('Луна', '2021-03-23', 'Молодая сибирская кошка', false, 1, null);

-- changeSet x3imal:63
INSERT INTO shelters(
    id, shelter_type, contacts, security_contacts, working_hours)
VALUES (2, 'dog', 'г. Астана, ул. Спасская, 8-800-888-88-88', '8(999)999-99-99', 'ежедневно, 09:00–21:00');

INSERT INTO pets (pet_name, birthday, description, is_adopted, shelter_id, photo_id)
VALUES
    ('Паф Деди', '2018-11-20', 'Любопытный мопс с веселым характером', false, 2, NULL),
    ('Иосиф Спалин', '2017-08-18', 'Спокойный и мудрый пес. Порода корги.', false, 2, NULL),
    ('Лисек', '2020-11-06', 'Жизнерадостный щенок с бесконечной энергией. Порода лайка.', false, 2, NULL),
    ('Роберт', '2017-07-06', 'Мягкий и ласковый щенок, немецкой авчарки.', false, 2, NULL);

-- changeSet annabelousova:65
INSERT INTO users(
	id, user_name, email, phone, is_volunteer)
	VALUES (788226796, 'anna_belou', 'anna@gmail.com', '+79155206396', true);
