CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INT NOT NULL CHECK (price >= 0),
    image_url VARCHAR(255) NOT NULL,
    stock     INT NOT NULL CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS point
(
    id           BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    earned_point INT NOT NULL CHECK (earned_point >= 0),
    left_point   INT NOT NULL CHECK (left_point >= 0),
    member_id    BIGINT   NOT NULL,
    expired_at   DATETIME NOT NULL,
    created_at   DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT   NOT NULL,
    point_id     BIGINT   NOT NULL,
    earned_point INT NOT NULL CHECK (earned_point >= 0),
    used_point   INT NOT NULL CHECK (used_point >= 0),
    created_at   DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS order_detail
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT NOT NULL CHECK (product_price >= 0),
    product_image_url VARCHAR(255) NOT NULL,
    order_quantity    INT NOT NULL CHECK (order_quantity >= 0)
);
