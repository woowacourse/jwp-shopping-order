DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS coupon;

CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE cart_item
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quantity      INT          NOT NULL,
    product_id    BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    product_price INT          NOT NULL,
    image_url     VARCHAR(255) NOT NULL,
    member_id     BIGINT       NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE orders
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE order_item
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quantity      INT          NOT NULL,
    product_id    BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    product_price INT          NOT NULL,
    image_url     VARCHAR(255) NOT NULL,
    order_id      BIGINT       NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE coupon
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    discount_type     VARCHAR(30)  NOT NULL,
    target_type       VARCHAR(30)  NOT NULL,
    target_product_id BIGINT,
    coupon_value             INT          NOT NULL,
    member_id         BIGINT       NOT NULL
);
