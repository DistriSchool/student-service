-- V3: add semester column to courses

ALTER TABLE students
ADD COLUMN semester INTEGER;

CREATE INDEX IF NOT EXISTS idx_students_semester ON students(semester);
