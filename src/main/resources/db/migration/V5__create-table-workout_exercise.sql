CREATE TABLE workout_exercise(
    id VARCHAR(255) NOT NULL PRIMARY KEY UNIQUE,
    workout_id VARCHAR(255) NOT NULL,
    exercise_id VARCHAR(255) NOT NULL,
    weight int NOT NULL,
    sets int NOT NULL,
    reps int NOT NULL,
    notes text,
    created_on DATETIME NOT NULL,
    update_on DATETIME,
    FOREIGN KEY (workout_id) REFERENCES workout(id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(id)
)