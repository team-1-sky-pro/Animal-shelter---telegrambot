-- liquibase formatted sql:

-- changeSet Rnd-mi:30
ALTER TABLE pets
RENAME COLUMN isAdopted TO is_adopted
