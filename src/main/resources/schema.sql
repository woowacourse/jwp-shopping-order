CREATE TABLE if not exists product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE if not exists orders
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE if not exists order_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    quantity   INT          NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE if not exists order_item_member_coupon
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_item_id    BIGINT NOT NULL,
    member_coupon_id BIGINT NOT NULL,
    FOREIGN KEY (order_item_id) REFERENCES order_item (id)
);

CREATE TABLE if not exists coupon
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    amount          INT          NOT NULL,
    discount_policy VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists member_coupon
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    owner_id  BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    is_used   BOOL   NOT NULL default FALSE,
    FOREIGN KEY (owner_id) REFERENCES member (id)
);
