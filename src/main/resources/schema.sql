CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE `order`
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    order_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment    INT       NOT NULL,
    used_point INT       NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE order_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    order_at   INT    NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE member_point
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    point      INT       NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (id)
);
