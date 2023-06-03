CREATE TABLE IF NOT EXISTS product
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         INT          NOT NULL,
    image_url     VARCHAR(255) NOT NULL,
    is_discounted TINYINT      NOT NULL,
    discount_rate INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email                 VARCHAR(255) NOT NULL UNIQUE,
    password              VARCHAR(255) NOT NULL,
    grade                 VARCHAR(255) NOT NULL,
    total_purchase_amount INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quantity   INT    NOT NULL,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS orders

(
    id                          BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    total_item_price            INT      NOT NULL,
    discounted_total_item_price INT      NOT NULL,
    shipping_fee                INT      NOT NULL,
    ordered_at                  DATETIME NOT NULL,
    member_id                   BIGINT   NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS order_item
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         INT          NOT NULL,
    image_url     VARCHAR(255) NOT NULL,
    quantity      INT          NOT NULL,
    discount_rate INT          NOT NULL,
    order_id      BIGINT       NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
)
