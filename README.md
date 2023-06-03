# jwp-shopping-order

## 기능 목록
- [x]  전체 쿠폰 반환한다.
- [x]  사용자 별로 쿠폰 발급한다
- [x]  사용자 별 사용 가능한 쿠폰 반환한다.
- [x]  장바구니에 상품을 주문한다
    - [x]  쿠폰을 적용한다.
        - [x]  쿠폰 사용 표시한다.
    - [x]  금액을 계산한다.
- [x]  사용자 별 주문 목록
- [x]  주문 별 상세 페이지

## DB
### schema.sql
```sql
CREATE TABLE IF NOT EXISTS product (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS member (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT UNSIGNED NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT UNSIGNED NOT NULL,
    total_price INT UNSIGNED NOT NULL,
    payment_price INT UNSIGNED NOT NULL,
    point INT UNSIGNED DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ordered_item (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT UNSIGNED NOT NULL,
    product_quantity INT UNSIGNED NOT NULL,
    product_image TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    min_amount INT UNSIGNED DEFAULT 0,
    discount_percent DECIMAL(5, 2) CHECK (discount_percent >= 0 AND discount_percent <= 100),
    discount_amount INT UNSIGNED NOT NULL,
    CONSTRAINT chk_coupon CHECK ((discount_percent = 0 AND discount_amount <> 0) OR (discount_percent <> 0 AND discount_amount = 0))
    );



CREATE TABLE IF NOT EXISTS member_coupon (
    id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT UNSIGNED NOT NULL,
    coupon_id BIGINT UNSIGNED NOT NULL,
    status    TINYINT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS point_history (
    id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT UNSIGNED NOT NULL,
    order_id  BIGINT UNSIGNED NOT NULL,
    used_point INT UNSIGNED DEFAULT 0,
    earned_point INT UNSIGNED DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ordered_coupon (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    member_coupon_id BIGINT UNSIGNED NOT NULL
);

```
### data.sql
```sql
INSERT INTO product (name, price, image_url)
VALUES ('치킨', 10000,
        'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url)
VALUES ('샐러드', 20000,
        'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url)
VALUES ('피자', 13000,
        'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (id, name, email, password)
VALUES (10L, '디노', 'dino@gmail.com', 'dino123');

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (2, 3, 5);

INSERT INTO coupon (id, name, min_amount, discount_percent, discount_amount)
VALUES (1L, '깜짝 쿠폰 -10%', 1000, 10, 0);
INSERT INTO coupon (id, name, min_amount, discount_percent, discount_amount)
VALUES (2L, '깜짝 쿠폰 -1000원', 100, 0, 1000);
INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount)
VALUES (4L, '웰컴 쿠폰 - 10%할인', 10000, 10, 0);
INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount)
VALUES (5L, '또 와요 쿠폰 - 3000원 할인', 15000, 0, 3000);

INSERT INTO member_coupon (id, member_id, coupon_id, status)
VALUES (1L, 10L, 1L, 1);

INSERT INTO point_history (id, member_id, order_id, used_point, earned_point)
VALUES (2L, 1L, 10L, 0, 100000);

```
