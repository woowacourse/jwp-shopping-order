CREATE TABLE policy
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    isPercentage BOOLEAN NOT NULL,
    amount       INT     NOT NULL
);

CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    isOnSale  BOOLEAN      NOT NULL,
    salePrice int
);

CREATE TABLE product_sale
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    policy_id  BIGINT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (policy_id) REFERENCES policy (id) ON DELETE CASCADE
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE coupon
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    policy_id BIGINT       NOT NULL,
--     member_id BIGINT       NOT NULL,
    member_id BIGINT       NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policy (id),
--     FOREIGN KEY (member_id) REFERENCES member (id)
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- 추가 테이블
-- CREATE TABLE member_coupon
-- (
--     id        BIGINT PRIMARY KEY AUTO_INCREMENT,
--     coupon_id BIGINT NOT NULL,
--     member_id BIGINT NOT NULL,
--     FOREIGN KEY (coupon_id) REFERENCES coupon (id),
--     FOREIGN KEY (member_id) REFERENCES member (id)
-- );

CREATE TABLE cart
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE order_table
(
    id           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT    NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    delivery_fee INT       NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE order_item_history
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id     BIGINT       NOT NULL,
    product_name   VARCHAR(255) NOT NULL,
    image_url      VARCHAR(255) NOT NULL,
    price          INT          NOT NULL,
    quantity       INT          NOT NULL,
    order_table_id BIGINT       NOT NULL,
    FOREIGN KEY (order_table_id) REFERENCES order_table (id)
);

CREATE TABLE coupon_history
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    order_table_id BIGINT       NOT NULL,
    FOREIGN KEY (order_table_id) REFERENCES order_table (id)
);
