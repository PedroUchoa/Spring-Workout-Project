CREATE TABLE body_weight(
    id VARCHAR(255) NOT NULL PRIMARY KEY UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    value int NOT NULL,
    is_active BOOLEAN NOT NULL,
    delete_on DATETIME,
    logged_on DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
)