DROP TABLE IF EXISTS `cart_item`;
DROP TABLE IF EXISTS `order_coupon`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `member_coupon`;
DROP TABLE IF EXISTS `coupon`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `product`;

CREATE TABLE IF NOT EXISTS `product`
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `member`
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `cart_item`
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES `member` (id),
    FOREIGN KEY (product_id) REFERENCES `product` (id)
);

CREATE TABLE IF NOT EXISTS `coupon`
(
    id                   BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                 varchar(50) NOT NULL,
    discount_value       INT         NOT NULL,
    minimum_order_amount INT         NOT NULL,
    end_date             DATETIME    NOT NULL
);

CREATE TABLE IF NOT EXISTS `member_coupon`
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES `member` (id),
    FOREIGN KEY (coupon_id) REFERENCES `coupon` (id)
);

CREATE TABLE IF NOT EXISTS `order`
(
    id          BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT   NOT NULL,
    delivery_fee INT NOT NULL,
    payed_price INT      NOT NULL,
    order_date  DATETIME NOT NULL,
    FOREIGN KEY (member_id) REFERENCES `member` (id)
);

CREATE TABLE IF NOT EXISTS `order_coupon`
(
    id BIGINT NOT NULL  AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (coupon_id) REFERENCES `coupon` (id)
);

CREATE TABLE IF NOT EXISTS `order_item`
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id  BIGINT       NOT NULL,
    product_id BIGINT NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    quantity  INT          NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order` (id)
);