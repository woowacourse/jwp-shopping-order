DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    point    INT          NOT NULL DEFAULT 0
);

CREATE TABLE product
(
    id        BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255)  NOT NULL,
    price     INT           NOT NULL,
    image_url VARCHAR(2083) NOT NULL,
    stock     INT           NOT NULL
);

CREATE TABLE cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL DEFAULT 1,

    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE orders
(
    id                  BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at          TIMESTAMP NOT NULL,
    member_id           BIGINT    NOT NULL,
    total_product_price INT       NOT NULL,
    total_delivery_fee  INT       NOT NULL,
    use_point           INT       NOT NULL,
    total_price         INT       NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE order_item
(
    id                BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT        NOT NULL,
    product_id        BIGINT        NOT NULL,
    product_name      VARCHAR(255)  NOT NULL,
    product_price     INT           NOT NULL,
    product_image_url VARCHAR(2083) NOT NULL,
    quantity          INT           NOT NULL,

    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);
