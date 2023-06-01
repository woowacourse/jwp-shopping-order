CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    money    INT          NOT NULL,
    point    INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    used_money INT       NOT NULL,
    used_point INT       NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_detail
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id        BIGINT NOT NULL,
    product_name     TEXT   NULL,
    product_image    TEXT   NULL,
    product_quantity INT    NULL,
    product_price    INT    NULL
);
