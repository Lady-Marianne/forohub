-- 1. Eliminar la clave foránea existente:
ALTER TABLE topics
DROP FOREIGN KEY fk_topics_courses;

-- 2. Modificar la columna `course_id` en `topics` a BIGINT NOT NULL
ALTER TABLE topics
MODIFY course_id BIGINT NOT NULL;

-- 3. Modificar la columna `course_id` en `courses` a BIGINT NOT NULL
ALTER TABLE courses
MODIFY course_id BIGINT NOT NULL;

-- 4. Volver a crear la clave foránea
ALTER TABLE topics
ADD CONSTRAINT fk_topics_courses FOREIGN KEY (course_id) REFERENCES courses(course_id);

