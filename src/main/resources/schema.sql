CREATE TABLE IF NOT EXISTS product (
    id INT UNSIGNED AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS member (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS cart_item (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS coupon
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    discount_rate INT NOT NULL,
    `period` INT NOT NULL,
    expired_date DATETIME NOT NULL,
    UNIQUE (name, discount_rate),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS member_coupon
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id INT UNSIGNED NOT NULL,
    coupon_id INT UNSIGNED NOT NULL,
    issued_date DATETIME NOT NULL,
    expired_date DATETIME NOT NULL,
    is_used TINYINT(1) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE TABLE IF NOT EXISTS `order`
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id INT UNSIGNED NOT NULL,
    total_product_price INT UNSIGNED NOT NULL,
    delivery_price INT UNSIGNED NOT NULL,
    order_date DATETIME NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS order_product
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    ordered_product_price BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS order_coupon
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id INT UNSIGNED NOT NULL,
    coupon_id INT UNSIGNED NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);
