DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS coupon_type;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;

CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
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

CREATE TABLE coupon_type
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    description     VARCHAR(255) NOT NULL,
    discount_amount INT          NOT NULL
);

CREATE TABLE coupon
(
    id             BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usage_status   VARCHAR(1) NOT NULL,
    member_id      BIGINT  NOT NULL,
    coupon_type_id BIGINT  NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_type_id) REFERENCES coupon_type (id)
);

CREATE TABLE order_item
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    price        INT    NOT NULL,
    cart_item_id BIGINT NOT NULL,
    coupon_id    BIGINT NOT NULL,
    FOREIGN KEY (cart_item_id) REFERENCES cart_item (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);
