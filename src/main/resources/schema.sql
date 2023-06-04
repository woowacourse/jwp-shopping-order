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

CREATE TABLE purchase_order
(
    id         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT      NOT NULL,
    order_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment    INT         NOT NULL,
    used_point INT         NOT NULL,
    status     VARCHAR(50) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE purchase_order_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    quantity   INT          NOT NULL,
    FOREIGN KEY (order_id) REFERENCES purchase_order (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE member_reward_point
(
    id              BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id       BIGINT    NOT NULL,
    point           INT       NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expired_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reward_order_id BIGINT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (reward_order_id) REFERENCES purchase_order (id)
);

CREATE TABLE order_member_used_point
(
    id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id             BIGINT NOT NULL,
    used_reward_point_id BIGINT NOT NULL,
    used_point           INT    NOT NULL,
    FOREIGN KEY (order_id) REFERENCES purchase_order (id),
    FOREIGN KEY (used_reward_point_id) REFERENCES member_reward_point (id)
);
