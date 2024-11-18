CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    about VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    verificationCode VARCHAR(255),
    isEnable BOOLEAN DEFAULT FALSE,
    avatarUrl VARCHAR(255)
);

insert into users (email, username, password, role, isEnable)
values  ('scasproject66@gmail.com','admin', 'Qwe12345', 'ADMIN', 'true')

