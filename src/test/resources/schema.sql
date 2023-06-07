CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     double       NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    money    double       NOT NULL,
    point    double       NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   double NOT NULL
);

CREATE TABLE IF NOT EXISTS order_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id  BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    member_id  BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      double       NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    quantity   double       NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT    NOT NULL,
    total_price  double    NOT NULL,
    use_point    double    NOT NULL,
    delivery_fee double    NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
