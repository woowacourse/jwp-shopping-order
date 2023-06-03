DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS product_order;
DROP TABLE IF EXISTS member_coupon;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;

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
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS `order`
(
    id                BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id         BIGINT NOT NULL,
    total_amount      INT    NOT NULL,
    discounted_amount INT    NOT NULL,
    address           VARCHAR(255),
    delivery_amount   INT,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS product_order
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id)
);

CREATE TABLE IF NOT EXISTS coupon
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    min_amount      INT         NOT NULL,
    discount_amount INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS member_coupon
(
    id        BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    is_used   BOOLEAN NOT NULL,
    member_id BIGINT  NOT NULL,
    coupon_id BIGINT  NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);
