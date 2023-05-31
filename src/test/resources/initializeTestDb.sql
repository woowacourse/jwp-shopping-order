--ALTER TABLE cart_item DROP CONSTRAINT CONSTRAINT_B48;
--ALTER TABLE cart_item DROP CONSTRAINT CONSTRAINT_B4;
--ALTER TABLE orders DROP CONSTRAINT CONSTRAINT_8B7;
DROP TABLE cart_item;
DROP TABLE order_items;
DROP TABLE orders;
DROP TABLE product;
DROP TABLE member;


CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders
(
    id             BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id      BIGINT   NOT NULL,
    product_price  BIGINT   NOT NULL,
    discount_price BIGINT   NOT NULL,
    delivery_fee   BIGINT   NOT NULL,
    total_price    BIGINT   NOT NULL,
    created_at     DATETIME NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS order_items
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    product_quantity  INTEGER      NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);


INSERT INTO member (email, password) VALUES ('user1@ddd.com', '1234');
INSERT INTO member (email, password) VALUES ('user2@ddd.com', '1234');
INSERT INTO product (name, price, image_url) VALUES ('1번 상품', 1000 , '1번 상품url');
INSERT INTO product (name, price, image_url) VALUES ('2번 상품', 2000 , '2번 상품url');
INSERT INTO product (name, price, image_url) VALUES ('3번 상품', 1000 , '3번 상품url');
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 3, 3);
