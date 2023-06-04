DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS member_coupon;
DROP TABLE IF EXISTS order_coupon;
DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS product
(
    id         INT UNSIGNED AUTO_INCREMENT,
    name       VARCHAR(20)  NOT NULL,
    price      INT          NOT NULL,
    image_url  VARCHAR(512) NOT NULL,
    is_deleted TINYINT(1)   NOT NULL DEFAULT (0),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member
(
    id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name     VARCHAR(10)  NOT NULL UNIQUE,
    password VARCHAR(64)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id  INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    quantity   INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS coupon
(
    id            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name          VARCHAR(50)  NOT NULL,
    discount_rate INT          NOT NULL,
    `period`      INT          NOT NULL,
    expired_at    DATETIME     NOT NULL,
    UNIQUE (name, discount_rate),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member_coupon
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id  INT UNSIGNED NOT NULL,
    coupon_id  INT UNSIGNED NOT NULL,
    issued_at  DATETIME     NOT NULL,
    expired_at DATETIME     NOT NULL,
    is_used    TINYINT(1)   NOT NULL,
    UNIQUE (member_id, coupon_id),
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);

CREATE TABLE IF NOT EXISTS `order`
(
    id                     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id              INT UNSIGNED NOT NULL,
    total_price            INT UNSIGNED NOT NULL,
    discounted_total_price INT UNSIGNED NOT NULL,
    delivery_price         INT UNSIGNED NOT NULL,
    ordered_at             DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS order_product
(
    id                    INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    order_id              INT UNSIGNED    NOT NULL,
    product_id            INT UNSIGNED    NOT NULL,
    ordered_product_price BIGINT UNSIGNED NOT NULL,
    quantity              INT             NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS order_coupon
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id  INT UNSIGNED NOT NULL,
    coupon_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);
