DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS point;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    stock     INT NOT NULL
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE point
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    earned_point INT NOT NULL,
    left_point   INT NOT NULL,
    member_id    BIGINT       NOT NULL,
    expired_at   DATETIME     NOT NULL,
    created_at   DATETIME     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE cart_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    quantity   INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE orders
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT       NOT NULL,
    point_id     BIGINT       NOT NULL,
    earned_point INT NOT NULL,
    used_point   INT NOT NULL,
    created_at   DATETIME     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (point_id) REFERENCES point (id)
);

CREATE TABLE order_detail
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    order_quantity    INT NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);
