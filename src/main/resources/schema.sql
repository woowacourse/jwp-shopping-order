CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
--     created_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
--     created_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
--     created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--         FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE order_history
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE order_product
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_history_id BIGINT NOT NULL,
    cart_item_id     BIGINT NOT NULL,

    FOREIGN KEY (order_history_id) REFERENCES order_history (id),
    FOREIGN KEY (cart_item_id) REFERENCES cart_item (id)
);

CREATE TABLE point
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    point_amount INT    NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id)
);
