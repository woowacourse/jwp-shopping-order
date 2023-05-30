CREATE TABLE product
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    stock      BIGINT       NOT NULL,
    isDelete   BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL
                       DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL
                       DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE member
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    point      INT DEFAULT 0,
    created_at TIMESTAMP    NOT NULL
                   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL
                   DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE cart_item
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    quantity   INT       NOT NULL,
    created_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE orders
(
    id         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE order_item
(
    id             BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id      BIGINT    NOT NULL,
    product_id     BIGINT    NOT NULL,
    quantity       INT       NOT NULL,
    original_name  INT       NOT NULL,
    original_price INT       NOT NULL,
    created_at     TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (orders_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE payment
(
    id                   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id            BIGINT NOT NULL,
    total_product_price  BIGINT NOT NULL,
    total_delivery_price BIGINT NOT NULL,
    use_point            BIGINT DEFAULT 0,
    total_payment        BIGINT NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id)
);
