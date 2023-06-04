CREATE TABLE IF NOT EXISTS product
(
    id         BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name       varchar(255)       NOT NULL,
    image_url  varchar(1024)      NOT NULL,
    price      long               NOT NULL,
    created_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS member
(
    id         BIGINT PRIMARY KEY  NOT NULL AUTO_INCREMENT,
    email      varchar(255) UNIQUE NOT NULL,
    password   varchar(255)        NOT NULL,
    created_at timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    member_id  BIGINT             NOT NULL,
    product_id BIGINT             NOT NULL,
    quantity   long               NOT NULL,
    created_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    delivery_fee long               NOT NULL,
    coupon_id    BIGINT,
    member_id    BIGINT             NOT NULL,
    created_at   timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item
(
    id         BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name       varchar(255)       NOT NULL,
    image_url  varchar(1024)      NOT NULL,
    price      long               NOT NULL,
    quantity   long               NOT NULL,
    order_id   BIGINT             NOT NULL,
    created_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupon
(
    id             BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name           varchar(255)       NOT NULL,
    policy_type    varchar(255)       NOT NULL,
    discount_value long               NOT NULL,
    minimum_price  long               NOT NULL,
    member_id      BIGINT             NOT NULL,
    used           boolean            NOT NULL,
    created_at     timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE cart_item
    ADD FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE cart_item
    ADD FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE orders
    ADD FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE order_item
    ADD FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE coupon
    ADD FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE orders
    ADD FOREIGN KEY (coupon_id) REFERENCES coupon (id);
