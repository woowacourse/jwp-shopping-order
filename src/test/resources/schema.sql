DROP TABLE IF EXISTS `cart_item`;
DROP TABLE IF EXISTS `order_product`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `member`;

CREATE TABLE `product`
(
    `id`        BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`      VARCHAR(255) NOT NULL,
    `price`     INT          NOT NULL,
    `image_url` VARCHAR(255) NOT NULL
);

CREATE TABLE `member`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email`    VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `point`    INT DEFAULT 0
);

CREATE TABLE `cart_item`
(
    `id`         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `member_id`  BIGINT    NOT NULL,
    `product_id` BIGINT    NOT NULL,
    `quantity`   INT       NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);

CREATE TABLE `orders`
(
    `id`         BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `member_id`  BIGINT    NOT NULL,
    `used_point` INT       NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);

CREATE TABLE `order_product`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id`          BIGINT       NOT NULL,
    `product_id`        BIGINT       NOT NULL,
    `product_name`      VARCHAR(255) NOT NULL,
    `product_price`     INT          NOT NULL,
    `product_image_url` VARCHAR(255) NOT NULL,
    `quantity`          INT          NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);
