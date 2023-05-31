CREATE TABLE IF NOT EXISTS product (
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     BIGINT       NOT NULL,
    image_url VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS member (
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS cart_item (
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id              BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id       BIGINT NOT NULL,
    delivery_fee    BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_item (
    id         BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   BIGINT        NOT NULL,
    name       VARCHAR(255)  NOT NULL,
    price      BIGINT        NOT NULL,
    image_url  VARCHAR(255)  NOT NULL,
    quantity   INT           NOT NULL
    );
