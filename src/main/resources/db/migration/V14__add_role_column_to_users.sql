-- V1__Add_role_column_to_users.sql
ALTER TABLE users
ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'USER';
