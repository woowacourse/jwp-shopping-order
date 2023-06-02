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
    password VARCHAR(255) NOT NULL,
    point    INT
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    CONSTRAINT EMPPK FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT EMPPK_2 FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders
(
    id            BIGINT    NOT NULL AUTO_INCREMENT,
    member_id     BIGINT    NOT NULL,
    total_payment INT       NOT NULL,
    used_point    INT       NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT EMPPK_3 FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_item
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    order_id          BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    quantity          INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT EMPPK_4 FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);
