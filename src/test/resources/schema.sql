CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
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
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT      NOT NULL,
    order_at     TIMESTAMP   NOT NULL,
    pay_amount   INT         NOT NULL,
    order_status VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_product
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE IF NOT EXISTS point_addition
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    order_id   BIGINT    NOT NULL,
    amount     INT       NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expire_at  TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS point_usage
(
    id                BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id         BIGINT NOT NULL,
    order_id          BIGINT NOT NULL,
    point_addition_id BIGINT NOT NULL,
    amount            INT    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_member_id_point_addition ON point_addition (member_id);
CREATE INDEX IF NOT EXISTS idx_member_created_at_point_addition ON point_addition (member_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_member_id_point_usage ON point_usage (member_id);
