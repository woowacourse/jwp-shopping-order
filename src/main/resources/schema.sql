CREATE TABLE product
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    price        INT          NOT NULL,
    image_url    VARCHAR(255) NOT NULL
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
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE coupon
(
    id             BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    coupon_name    VARCHAR(255)   NOT NULL,
    discount_type  VARCHAR(10)    NOT NULL,
    discount_value DECIMAL(10, 3) NOT NULL
);

CREATE TABLE member_coupon
(
    id        BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    is_used   BOOLEAN NOT NULL,
    member_id BIGINT  NOT NULL,
    coupon_id BIGINT  NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupon (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE orders
(
    id             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    original_price INT    NOT NULL,
    actual_price   INT    NOT NULL,
    delivery_fee   INT    NOT NULL,
    member_id      BIGINT NOT NULL
);

CREATE TABLE orders_item
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price        INT          NOT NULL,
    image_url    VARCHAR(255) NOT NULL,
    quantity     INT          NOT NULL,
    product_id   BIGINT       NOT NULL,
    orders_id     BIGINT       NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE orders_coupon
(
    id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    coupon_name      VARCHAR(255) NOT NULL,
    discount_amount  INT          NOT NULL,
    member_coupon_id BIGINT       NOT NULL,
    orders_id         BIGINT       NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id) ON UPDATE CASCADE ON DELETE CASCADE
);
