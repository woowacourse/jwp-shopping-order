CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url TEXT         NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    points   INT          NOT NULL DEFAULT 0
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
    id            BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT   NOT NULL,
    earned_points INT      NOT NULL,
    used_points   INT      NOT NULL,
    total_price   INT      NOT NULL,
    pay_price     INT      NOT NULL,
    order_date    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    image_url  TEXT         NOT NULL,
    quantity   INT          NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

