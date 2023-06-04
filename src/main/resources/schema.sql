CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255)    NOT NULL UNIQUE,
    password VARCHAR(255)    NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT          NOT NULL,
    product_id BIGINT          NOT NULL,
    quantity   INT             NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    member_id    BIGINT      NOT NULL,
    coupon_id    BIGINT,
    delivery_fee BIGINT      NOT NULL,
    status       VARCHAR(10) NOT NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item
(
    id        BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    order_id  BIGINT       NOT NULL,
    name      VARCHAR(255) NOT NULL,
    price     BIGINT       NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    quantity  INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS coupon_type
(
    id              BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    discount_type   VARCHAR(10)  NOT NULL,
    discount_amount BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS coupon
(
    id             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    member_id      BIGINT  NOT NULL,
    coupon_type_id BIGINT  NOT NULL,
    is_used        BOOLEAN NOT NULL DEFAULT FALSE
);
