CREATE TABLE IF NOT EXISTS tb_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_member (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    point INT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES tb_member(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES tb_product(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_order (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    earned_points INT NOT NULL,
    used_points INT NOT NULL,
    total_price INT NOT NULL,
    pay_price INT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    FOREIGN KEY (member_id) REFERENCES tb_member(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_order_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_image_url TEXT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES tb_order(id) ON DELETE CASCADE
);
