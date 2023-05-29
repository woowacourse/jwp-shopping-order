# jwp-shopping-order

# 기능 목록

- [ ] 장바구니에 담은 상품을 주문할 수 있다.
- [ ] 포인트를 사용할 수 있다.
- [ ] 사용자 별로 주문 목록을 확인할 수 있다.
- [ ] 특정 주문의 상세 정보를 확인할 수 있다.

# API 협의 결과

## 유저정보 조회 (포인트 조회)

### Request

```
GET /users HTTP/1.1
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json

{
	"email": "odo27@naver.com",
	“point”: 1000 // 현재 보유중인 포인트
	"earnRate": 5 // 적립률 5%
}
```

## 주문 요청

### Request

```
POST /orders HTTP/1.1
```

```
{
	"cartIds": [1, 2, 3], // 주문할 카트 아이디
	"point": 1500, // 사용할 포인트
	"totalPrice": 32000 // 총액
}
```

### Response (성공)

```
HTTP/1.1 201 OK
Content-Type: application/json
Location: "/orders/{id}"
```

### Response (실패: 재고 부족)

```
HTTP/1.1 409 Conflict
Content-Type: application/json

{
	"message": "1번 상품의 재고가 부족합니다."
}
```

### Response (실패: 포인트 부족)

```
HTTP/1.1 409 Conflict
Content-Type: application/json

{
	"message": "포인트가 부족합니다."
}
```

## 주문목록 조회

### Request

```
GET /orders HTTP/1.1
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json

[
	{
		"orderId": 1,
		"createdAt": 2023-05-26,
		"orderItems": [
				{
					"productId": 10,
					"productName": "새우깡",
					"quantity": 3,
					"price": 1500,
					"imageUrl": "http://example.com/dfdf",
				},
				{
					"productId": 22,
					"productName": "감자깡",
					"quantity": 1,
					"price": 1200,
					"imageUrl": "http://example.com/abcd",
				}
		],
		"totalPrice": 15000,
		"usedPoint": 1700,
		"earnedPoint": 300
	},
	{
		"orderId": 3,
		"createdAt": 2023-05-25,
		"orderItems": [
				{
					"productId": 10,
					"productName": "새우깡",
					"quantity": 3,
					"price": 1500,
					"imageUrl": "http://example.com/dfdf",
				},
				{
					"productId": 22,
					"productName": "감자깡",
					"quantity": 1,
					"price": 1200,
					"imageUrl": "http://example.com/abcd",
				}
		],
		"totalPrice": 15000,
		"usedPoint": 1700,
		"earnedPoint": 200
	}
]
```

## 주문 조회

### Request

```
GET /orders/{id} HTTP/1.1
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json

{
	"orderId": 1,
	"createdAt": 2023-05-26,
	"orderItems": [
			{
				"productId": 10,
				"productName": "새우깡",
				"quantity": 3,
				"price": 1500,
				"imageUrl": "http://example.com/dfdf",
			},
			{
				"productId": 22,
				"productName": "감자깡",
				"quantity": 1,
				"price": 1200,
				"imageUrl": "http://example.com/abcd",
			}
	],
	"totalPrice": 15000,
	"usedPoint": 1700,
	"earnedPoint": 300
}
```

# 데이터베이스 설계 협의 결과

```
CREATE TABLE product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT UNSIGNED NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    stock     INT UNSIGNED NOT NULL
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE point
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    earned_point INT UNSIGNED NOT NULL,
    left_point   INT UNSIGNED NOT NULL,
    member_id    BIGINT       NOT NULL,
    expired_at   DATETIME     NOT NULL,
    created_at   DATETIME     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE cart_item
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    product_id BIGINT       NOT NULL,
    quantity   INT UNSIGNED NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE orders
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT       NOT NULL,
    point_id     BIGINT       NOT NULL,
    earned_point INT UNSIGNED NOT NULL,
    used_point   INT UNSIGNED NOT NULL,
    created_at   DATETIME     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (point_id) REFERENCES point (id)
);

CREATE TABLE order_detail
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT UNSIGNED NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    order_quantity    INT UNSIGNED NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);
```
