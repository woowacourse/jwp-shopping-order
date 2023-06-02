CREATE TABLE member (
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE discount_policy (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    target ENUM('SPECIFIC', 'ALL', 'DELIVERY', 'TOTAL') NOT NULL,
    unit ENUM('PERCENTAGE', 'ABSOLUTE') NOT NULL,
    discount_value INT NOT NULL
);

CREATE TABLE coupon (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    discount_policy_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (discount_policy_id) REFERENCES discount_policy(id)
);

CREATE TABLE sale (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    discount_policy_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (discount_policy_id) REFERENCES discount_policy(id)
);

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    delivery_price INT NOT NULL,
    ordered_time VARCHAR(255) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE order_item(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    orders_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);

CREATE TABLE order_coupon (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    coupon_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
)


