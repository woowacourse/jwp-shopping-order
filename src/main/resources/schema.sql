CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url TEXT         NOT NULL
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL
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
    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    min_order_price     INT          NOT NULL,
    max_discount_price  INT          NOT NULL,
    type                VARCHAR(255) NOT NULL,
    discount_amount     INT,
    discount_percentage DOUBLE
);

CREATE TABLE member_coupon
(
    id         BIGINT                             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT                             NOT NULL,
    coupon_id  BIGINT                             NOT NULL,
    is_used    BOOLEAN  DEFAULT false             NOT NULL,
    expired_at DATETIME                           NOT NULL,
    created_at DATETIME DEFAULT current_timestamp NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);

CREATE TABLE `order`
(
    id               BIGINT                             NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT                             NOT NULL,
    member_coupon_id BIGINT,
    shipping_fee     INT                                NOT NULL,
    total_price      INT                                NOT NULL,
    created_at       DATETIME DEFAULT current_timestamp NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (member_coupon_id) REFERENCES member_coupon (id)
);

CREATE TABLE order_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    image_url  TEXT         NOT NULL,
    quantity   INT          NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order` (id)
);
