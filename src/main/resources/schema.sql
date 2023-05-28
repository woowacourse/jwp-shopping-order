CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL default 0
);

CREATE TABLE member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL
);

CREATE TABLE cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
