# jwp-shopping-order

# 기능 목록

- [x] 장바구니에 담은 상품을 주문할 수 있다.
    - [x] 주문을 하면 재고가 줄어든다.
- [x] 포인트를 사용할 수 있다.
- [x] 사용자 별로 주문 목록을 확인할 수 있다.
- [x] 특정 주문의 상세 정보를 확인할 수 있다.

# API 명세

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
	"email": "odo1@woowa.com",
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
	"errorCode": 1
	"message": "1번 상품의 재고가 부족합니다."
}
```

### Response (실패: 포인트 부족)

```
HTTP/1.1 409 Conflict
Content-Type: application/json

{
	"errorCode": 2
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

# 데이터베이스 환경설정

## Docker 사용 방법

1. docker directory를 생성한다.

2. 생성한 directory 하위에 docker-compose.yml 파일 생성

```
version: "3.9"
services:
  db:
    image: mysql:8.0.33
    platform: linux/amd64
    restart: always
    ports:
      - "13306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: cart
      MYSQL_USER: cart
      MYSQL_PASSWORD: password
      TZ: Asia/Seoul
```

3. docker-compose.yml 파일이 있는 경로에서, docker 명령어로 Server를 실행

```
# Docker 실행하기
docker-compose -p cart up -d
```

```
# Docker 정지하기
docker-compose -p cart down
```

## Local MYSQL 사용 방법

1. MYSQL WorkBench를 설치하고 실행한다.

2. 다음과 같이 연결 정보를 입력한다.

```
Hostname : localhost
Port : 13306
Username : root
```

3. 새로운 유저를 생성한다.

```
create user 'username'@'localhost' identified by 'password';
```

4. 생성한 유저에게 모든 db 및 테이블에 접근권한 부여

```
grant all privileges on *.* to 'username'@'localhost';
```

5. 설정한 권한 적용

```
flush privileges;
```

## 데이터베이스 생성 쿼리

1. 데이터베이스 `cart`를 만듭니다.

```
CREATE DATABASE cart DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
```

2. 테이블은 애플리케이션을 실행시키면 자동으로 생성됩니다.

## 👏👏👏 모든 설정을 완료했습니다!! 👏👏👏
