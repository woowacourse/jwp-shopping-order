CREATE TABLE product
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255)                        NOT NULL,
    price      INT                                 NOT NULL,
    image_url  VARCHAR(255)                        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE member
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    email      VARCHAR(255) UNIQUE                 NOT NULL,
    password   VARCHAR(255)                        NOT NULL,
    point      INT       DEFAULT 0                 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE cart_item
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id  BIGINT                              NOT NULL,
    product_id BIGINT                              NOT NULL,
    quantity   INT                                 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE order_history
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id    BIGINT                              NOT NULL,
    total_amount INT                                 NOT NULL,
    used_point   INT                                 NOT NULL,
    saved_point  INT                                 NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE order_product
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id   BIGINT                              NOT NULL,
    product_id BIGINT                              NOT NULL,
    quantity   INT                                 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (order_id) REFERENCES order_history (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);
