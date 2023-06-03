DROP TABLE product IF EXISTS;
DROP TABLE member IF EXISTS;
DROP TABLE cart_item IF EXISTS;
DROP TABLE orders IF EXISTS;
DROP TABLE order_item IF EXISTS;
DROP TABLE coupon_type IF EXISTS;
DROP TABLE coupon IF EXISTS;
DROP TABLE member_coupon IF EXISTS;

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
                        coupon_id BIGINT,
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


CREATE TABLE coupon_type (
                             id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(255) NOT NULL,
                             discount_type VARCHAR(10) NOT NULL,
                             discount_amount BIGINT NOT NULL
);

CREATE TABLE coupon (
                        id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
                        coupon_type_id BIGINT NOT NULL,
                        is_used BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE member_coupon (
                               member_id BIGINT NOT NULL,
                               coupon_id BIGINT NOT NULL
);
