CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT UNSIGNED NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    stock     INT UNSIGNED NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS point
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    earned_point INT UNSIGNED NOT NULL,
    left_point   INT UNSIGNED NOT NULL,
    member_id    BIGINT       NOT NULL,
    expired_at   DATETIME         NOT NULL,
    created_at   DATETIME         NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    quantity   INT UNSIGNED NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT       NOT NULL,
    point_id     BIGINT       NOT NULL,
    earned_point INT UNSIGNED NOT NULL,
    used_point   INT UNSIGNED NOT NULL,
    created_at   DATETIME         NOT NULL
);

CREATE TABLE IF NOT EXISTS order_detail
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT UNSIGNED NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    order_quantity    INT UNSIGNED NOT NULL
);
