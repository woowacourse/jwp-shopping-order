CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image VARCHAR(255) NOT NULL,
    is_discounted TINYINT NOT NULL,
    discount_rate INT
);

CREATE TABLE member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     rank VARCHAR(255),
     total_price BIGINT NOT NULL
);

CREATE TABLE cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    total_purchase_amount INT NOT NULL,
    total_item_price INT NOT NULL,
    ordered_at DATETIME NOT NULL default current_timestamp,
    shipping_fee INT NOT NULL,
    discounted_total_price INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE ordered_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_image VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL,
    product_is_discounted TINYINT NOT NULL,
    product_discount_rate INT
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);
