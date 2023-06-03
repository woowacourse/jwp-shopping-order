CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS  member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS  cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    UNIQUE KEY member_id_and_product_id (member_id, product_id),
    KEY cart_item_member_id_index (member_id),
    KEY cart_item_product_id_index (product_id)
);

CREATE TABLE IF NOT EXISTS  orders
(
    id             BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id      BIGINT    NOT NULL,
    actual_price   INT       NOT NULL,
    original_price INT       NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS  order_item
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT       NOT NULL,
    product_id    BIGINT       NOT NULL,
    product_name  VARCHAR(255) NOT NULL,
    product_image VARCHAR(255) NOT NULL,
    product_price INT          NOT NULL,
    quantity      INT          NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS  order_coupon
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id  BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS  discount_condition
(
    id                      BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    discount_condition_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS  amount_discount
(
    id   BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rate INT    NOT NULL
);

CREATE TABLE IF NOT EXISTS  discount_type
(
    id                 BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    discount_type      VARCHAR(255) NOT NULL,
    discount_amount_id BIGINT       NOT NULL,
    FOREIGN KEY (discount_amount_id) REFERENCES amount_discount (id)
);

CREATE TABLE IF NOT EXISTS  coupon
(
    id                    BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name                  VARCHAR(255) NOT NULL,
    discount_condition_id BIGINT       NOT NULL,
    discount_type_id      BIGINT       NOT NULL,
    FOREIGN KEY (discount_condition_id) REFERENCES discount_condition (id),
    FOREIGN KEY (discount_type_id) REFERENCES discount_type (id)
);

CREATE TABLE IF NOT EXISTS  coupon_member
(
    id        BIGINT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    coupon_id BIGINT  NOT NULL,
    member_id BIGINT  NOT NULL,
    FOREIGN KEY (coupon_id) REFERENCES coupon (id),
    KEY coupon_member_coupon_id_index (coupon_id),
    KEY coupon_member_member_id_index (member_id)
);
