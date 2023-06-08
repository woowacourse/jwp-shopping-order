CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT(0),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    is_deleted TINYINT(1) NOT NULL DEFAULT(0),
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS coupon
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    discount_rate INT NOT NULL,
    `period` INT NOT NULL,
    expired_at DATETIME NOT NULL,
    UNIQUE (name, discount_rate),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS member_coupon
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    issued_at DATETIME NOT NULL,
    expired_at DATETIME NOT NULL,
    is_used TINYINT(1) NOT NULL,
    PRIMARY KEY(id),
    UNIQUE(member_id, coupon_id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE TABLE IF NOT EXISTS `order`
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    total_price BIGINT NOT NULL,
    discounted_total_price BIGINT NOT NULL,
    delivery_price INT NOT NULL,
    ordered_at DATETIME NOT NULL,
    is_valid TINYINT(1) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS order_product
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    ordered_product_price BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS order_coupon
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);
