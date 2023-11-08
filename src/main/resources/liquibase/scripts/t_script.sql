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


-- changeSet annabelousova:50
ALTER TABLE users
ALTER COLUMN phone TYPE VARCHAR(50);
ALTER TABLE users
DROP CONSTRAINT ch_phone;

