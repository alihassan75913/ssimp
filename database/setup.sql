CREATE DATABASE IF NOT EXISTS ssimp;

USE ssimp;

SELECT DATABASE() AS database_name;

USE ssimp;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('USER', 'AGENT', 'ADMIN') NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DESCRIBE users;

USE ssimp;

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    priority ENUM('LOW', 'NORMAL', 'HIGH')
        NOT NULL DEFAULT 'NORMAL',
    status ENUM('NEW', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')
        NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_tickets_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_tickets_category
        FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

INSERT IGNORE INTO categories (name)
VALUES
    ('Internet'),
    ('Email'),
    ('Printer'),
    ('Account Access');

DESCRIBE tickets;

SELECT category_id, name
FROM categories
ORDER BY category_id;

USE ssimp;

CREATE TABLE IF NOT EXISTS suggestions (
    suggestion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    step_number INT NOT NULL,
    instruction VARCHAR(500) NOT NULL,

    CONSTRAINT uq_suggestions_category_step
        UNIQUE (category_id, step_number),

    CONSTRAINT fk_suggestions_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
);

INSERT IGNORE INTO suggestions
    (category_id, step_number, instruction)

SELECT category_id, 1,
    'Check that Wi-Fi is turned on or that your network cable is connected.'
FROM categories WHERE name = 'Internet'

UNION ALL

SELECT category_id, 2,
    'Try opening another website to check whether the problem affects more than one site.'
FROM categories WHERE name = 'Internet'

UNION ALL

SELECT category_id, 1,
    'Check that your internet connection is working.'
FROM categories WHERE name = 'Email'

UNION ALL

SELECT category_id, 2,
    'If you cannot sign in, check that you entered your email address correctly.'
FROM categories WHERE name = 'Email'

UNION ALL

SELECT category_id, 1,
    'Check that the printer is switched on and has paper.'
FROM categories WHERE name = 'Printer'

UNION ALL

SELECT category_id, 2,
    'Check that the correct printer is selected before printing.'
FROM categories WHERE name = 'Printer'

UNION ALL

SELECT category_id, 1,
    'Check that you are using the correct email address and that Caps Lock is off.'
FROM categories WHERE name = 'Account Access'

UNION ALL

SELECT category_id, 2,
    'If the message says your account is locked or disabled, include that message in your ticket.'
FROM categories WHERE name = 'Account Access';

SELECT
    c.name AS category,
    s.step_number,
    s.instruction
FROM suggestions s
JOIN categories c ON c.category_id = s.category_id
ORDER BY c.name, s.step_number;