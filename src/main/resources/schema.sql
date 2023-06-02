DROP TABLE product IF EXISTS;
DROP TABLE member IF EXISTS;
DROP TABLE cart_item IF EXISTS;
DROP TABLE orders IF EXISTS;
DROP TABLE order_item IF EXISTS;

CREATE TABLE product (
                         id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         price INT NOT NULL,
                         image_url VARCHAR(255) NOT NULL
);

CREATE TABLE member (
                        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
);

CREATE TABLE cart_item (
                           id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           member_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL
);

CREATE TABLE orders (
                        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                        member_id BIGINT NOT NULL,
                        delivery_fee BIGINT NOT NULL,
                        status VARCHAR(10) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_item (
                            id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                            order_id BIGINT NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            price BIGINT NOT NULL,
                            image_url VARCHAR(255) NOT NULL,
                            quantity INT NOT NULL
);
