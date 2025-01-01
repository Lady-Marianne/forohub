CREATE TABLE admins (
    user_id BIGINT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
