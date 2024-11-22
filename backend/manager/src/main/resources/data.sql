CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    about VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    verificationCode VARCHAR(255),
    is_enable BOOLEAN DEFAULT FALSE,
    avatar_url VARCHAR(255)
);

insert into users (email, username, password, role, is_enable, avatar_url)
values  ('scasproject66@gmail.com','admin', '$2a$12$cC6WLP9f8GXCVPDw3yFq8ODf9pnWh/jvM1Z3uEhU4N0ALU7CRSViW', 'ADMIN', 'true', 'http://localhost:9002/avatars/avatar_photo_5312079920823920430_y.jpg');
--Admin123

insert into users (email, username, password, role, is_enable)
values  ('sca@mail.ru', 'sca', '$2a$12$zdRAbmYZ0ttwVGDRkTBRBuRDOg9vg5P6SziW3S3FLN6PkEzKF6jou', 'USER', 'true')
--Qw123456
