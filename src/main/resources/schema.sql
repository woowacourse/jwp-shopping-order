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
    password VARCHAR(255) NOT NULL,
    point    INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS shopping_order
(
    id          BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT   NOT NULL,
    used_point  BIGINT   NOT NULL,
    saved_point BIGINT   NOT NULL,
    ordered_at  DATETIME NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS ordered_item
(
    id                         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id                 BIGINT       NOT NULL,
    shopping_order_id          BIGINT       NOT NULL,
    product_name_at_order      VARCHAR(255) NOT NULL,
    product_price_at_order     INT          NOT NULL,
    product_image_url_at_order VARCHAR(255) NOT NULL,
    quantity                   INT          NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (shopping_order_id) REFERENCES shopping_order (id)
);
