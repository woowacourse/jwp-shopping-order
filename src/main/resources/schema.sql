CREATE TABLE product
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255)  NOT NULL,
    price      BIGINT        NOT NULL,
    image_url  VARCHAR(2048) NOT NULL,
    is_deleted BOOL DEFAULT false
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
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE coupon
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    type            VARCHAR(255) NOT NULL,
    discount_value  BIGINT       NOT NULL,
    min_order_price BIGINT       NOT NULL
);

CREATE TABLE member_coupon
(
    id           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT    NOT NULL,
    coupon_id    BIGINT    NOT NULL,
    expired_date DATE NOT NULL,
    is_used      BOOL DEFAULT false,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupon (id) ON DELETE CASCADE
);

CREATE TABLE orders
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT       NOT NULL,
    coupon_id     BIGINT,
    orders_number VARCHAR(255) NOT NULL,
    delivery_fee  INT          NOT NULL,
    created_at    TIMESTAMP    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupon (id) ON DELETE CASCADE
);

CREATE TABLE orders_product
(
    id                BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT        NOT NULL,
    product_id        BIGINT        NOT NULL,
    quantity          INT           NOT NULL,
    product_name      VARCHAR(255)  NOT NULL,
    product_price     BIGINT        NOT NULL,
    product_image_url VARCHAR(2048) NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);
