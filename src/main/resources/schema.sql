drop table product if exists;
drop table member if exists;
drop table cart_item if exists;
drop table orders if exists;
drop table order_item if exists;
drop table coupon if exists;
drop table member_coupon if exists;

CREATE TABLE product
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     BIGINT       NOT NULL,
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
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE orders
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    delivery_fee BIGINT NOT NULL
);

CREATE TABLE order_item
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id  BIGINT       NOT NULL,
    name      varchar(255) NOT NULL,
    price     BIGINT       NOT NULL,
    image_url varchar(255) NOT NULL,
    quantity  INT          NOT NULL
);

CREATE TABLE coupon
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name           varchar(255) NOT NULL,
    discount_price BIGINT       NOT NULL
);

CREATE TABLE member_coupon
(
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    used_yn        varchar(1) DEFAULT 'N',
    primary key (member_id, coupon_id)
);

