-- liquibase formatted sql

-- changeSet Rnd-mi:30.1
ALTER TABLE pets
ALTER COLUMN birthday TYPE DATE
