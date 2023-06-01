CREATE TABLE product
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
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

CREATE TABLE order_history
(
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    original_price INT    NOT NULL,
    used_point  INT    NOT NULL,
    total_price INT    NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE order_product
(
    id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_history_id BIGINT       NOT NULL,
    product_id       BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    price            INT          NOT NULL,
    image_url        VARCHAR(255) NOT NULL,
    quantity         INT          NOT NULL,

    FOREIGN KEY (order_history_id) REFERENCES order_history (id) ON DELETE CASCADE
);

CREATE TABLE point
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    point_amount INT    NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);
