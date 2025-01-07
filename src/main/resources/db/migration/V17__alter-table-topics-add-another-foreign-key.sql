ALTER TABLE topics
ADD COLUMN course_id BIGINT,
ADD CONSTRAINT fk_topics_courses FOREIGN KEY (course_id) REFERENCES courses(course_id);
