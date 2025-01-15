-- Crear la tabla 'topics':

CREATE TABLE topics (
    topic_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'PENDING', 'CLOSED', 'ARCHIVED', 'DELETED') DEFAULT 'PENDING',
    author VARCHAR(100),
    course VARCHAR(100),
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);