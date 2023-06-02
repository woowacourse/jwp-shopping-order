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

CREATE TABLE if not exists coupon
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    type            VARCHAR(20) NOT NULL,
    discount_amount INT         NOT NULL
);

CREATE TABLE if not exists member_coupon
(
    id        BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    used      BOOLEAN NOT NULL,
    member_id BIGINT  NOT NULL,
    coupon_id BIGINT  NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);

CREATE TABLE if not exists orders
(
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    total_price BIGINT NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE if not exists order_item
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    quantity          INT          NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    total_price       INT          NOT NULL,

    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE if not exists order_coupon
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_item_id    BIGINT NOT NULL,
    member_coupon_id BIGINT NOT NULL,

    FOREIGN KEY (order_item_id) REFERENCES order_item (id),
    FOREIGN KEY (member_coupon_id) REFERENCES member_coupon (id)
);


