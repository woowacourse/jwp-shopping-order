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
