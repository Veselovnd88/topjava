DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(description, calories, date_time, user_id)
VALUES ('user meal', 1000, '2024-02-14 20:00:06', 100000),
       ('admin meal', 500, '2024-02-15 20:00:06', 100001),
       ('guest meal', 600, '2024-02-13 20:00:06', 100002);
