CREATE TABLE IF NOT EXISTS product
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255)    NOT NULL,
    price               INT             NOT NULL,
    image_url           VARCHAR(255)    NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email               VARCHAR(255)    NOT NULL UNIQUE,
    password            VARCHAR(255)    NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id           BIGINT          NOT NULL,
    product_id          BIGINT          NOT NULL,
    quantity            INT             NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id           BIGINT          NOT NULL FOREIGN KEY REFERENCES member(id),
    total_payment       DOUBLE          NOT NULL,
    used_point          INT             NOT NULL,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id            BIGINT          NOT NULL FOREIGN KEY REFERENCES orders(id),
    product_name        VARCHAR(255)    NOT NULL,
    product_price       INT             NOT NULL,
    product_image_url   VARCHAR(255)    NOT NULL,
    quantity            INT             NOT NULL
);
