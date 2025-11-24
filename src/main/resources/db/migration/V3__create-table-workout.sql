CREATE TABLE workout(
    id VARCHAR(255) NOT NULL PRIMARY KEY UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    notes TEXT NOT NULL,
    started_on DATETIME NOT NULL,
    finished_on DATETIME NOT NULL,
    created_on DATETIME NOT NULL,
    update_on DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id)
)