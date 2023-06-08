CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS order_record (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    order_time DATETIME NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS order_item(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    total_price INT NOT NULL,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_record(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment_record(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    original_total_price INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_record(id)
);

CREATE TABLE IF NOT EXISTS applied_discount_policy(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payment_record_id BIGINT NOT NULL,
    discount_policy_id BIGINT NOT NULL,
    FOREIGN KEY (payment_record_id) REFERENCES payment_record(id)
);

CREATE TABLE IF NOT EXISTS applied_delivery_policy(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payment_record_id BIGINT NOT NULL,
    delivery_policy_id BIGINT NOT NULL,
    FOREIGN KEY (payment_record_id) REFERENCES payment_record(id)
);