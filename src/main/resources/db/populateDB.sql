DELETE
FROM meals;
DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(description, calories, date_time, user_id)
VALUES ('user meal breakFast', 500, '2024-02-14 10:00:06', 100000),
       ('user meal lunch', 1000, '2024-02-14 13:00:06', 100000),
       ('user meal dinner', 500, '2024-02-14 20:00:06', 100000),
       ('user meal border', 500, '2024-02-15 00:00:00', 100000),
       ('user meal breakfast', 1000, '2024-02-15 10:00:06', 100000),
       ('user meal lunch', 500, '2024-02-15 13:00:06', 100000),
       ('user meal dinner', 410, '2024-02-15 20:00:06', 100000),

       ('admin meal breakFast', 500, '2024-02-14 10:00:06', 100001),
       ('admin meal lunch', 1000, '2024-02-14 13:00:06', 100001),
       ('admin meal dinner', 500, '2024-02-14 20:00:06', 100001),
       ('admin meal border', 500, '2024-02-15 00:00:00', 100001),
       ('admin meal breakfast', 1000, '2024-02-15 10:00:06', 100001),
       ('admin meal lunch', 500, '2024-02-15 13:00:06', 100001),
       ('admin meal dinner', 410, '2024-02-15 20:00:06', 100001);
