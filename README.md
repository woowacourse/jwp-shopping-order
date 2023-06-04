# jwp-shopping-order

## 요구사항 명세서

### 주문 목록 조회
- [x] 주문 목록에는 주문 번호, 최종 결제 금액, 주문 날짜, 상품 이름(한 개), 상품 url, 총 상품 개수를 전달한다.
    - [x] 주문 날짜는 date 타입으로 전달한다.
- [x] 한 페이지 당 10개씩 값을 전달받는다.
    - [x] 페이지 번호를 전달받을 때 그에 맞는 페이지의 주문 목록을 전달한다.
    - [x] 페이지 번호의 기준은 주문 시간 순이다.

### 주문 상세 정보
- [x] 주문 상세 정보에는 주문 번호, 주문 날짜, 최종 결제 금액, 사용한 포인트, 적립 포인트, 상품 정보를 전달한다.
    - [x] 주문 날짜는 date 타입으로 전달한다.
    - [x] 상품 정보는 장바구니에 담긴 상품 정보와 동일하다.

### 주문 요청
- [x] 주문 요청에는 사용된 포인트와 각 상품 id, 수량을 전달해 주문을 요청한다.

### 포인트
- [x] 상품 주문 시 포인트가 적립된다.
    - [x] 실제 주문 가격 5만원 미만은 실제 주문 가격의 5%가 적립된다.
    - [x] 실제 주문 가격 5만원 이상 10만원 미만은 실제 주문 가격의 8%가 적립된다.
    - [x] 실제 주문 가격 10만원 이상은 실제 주문 가격의 10%가 적립된다.
    - [x] 포인트의 유효기간은 3개월 뒤의 말 일까지이다.
	- [x] 주문 가격에서 포인트를 사용한다면 그 금액은 포인트 적립에서 제외한다. 
- [x] 포인트를 조회할 때 현재 남은 포인트와 이달 소멸될 포인트를 전달한다.

## json 명세서

### 주문 목록 조회 -> 10개씩

#### GET /orders?page=n HTTP/1.1

#### HTTP/1.1 200 OK

#### Content-Type: application/json

```http request
{
    "totalPages": 10,
    "currentPage": 5,
    "contents": [
        {
            "orderId": 1,
            "payAmount": 10000, // 최종 결제 금액 (전체 - 포인트)
            "orderAt": "2024-10-23",
            "orderStatus": "Pending",
            "productName": "마우스",
            "productImageUrl": "http://example.com/chicken.jpg",
            "totalProductCount": 4
        },
                {
            "orderId": 1,
            "payAmount": 10000, // 최종 결제 금액 (전체 - 포인트)
            "orderAt": "2024-10-23",
            "orderStatus": "Pending",
            "productName": "마우스",
            "productImageUrl": "http://example.com/chicken.jpg",
            "totalProductCount": 4
        },
                {
            "orderId": 1,
            "payAmount": 10000, // 최종 결제 금액 (전체 - 포인트)
            "orderAt": "2024-10-23",
            "orderStatus": "Pending",
            "productName": "마우스",
            "productImageUrl": "http://example.com/chicken.jpg",
            "totalProductCount": 4
        }
    ]
}
```

### 주문 상세 정보 조회
####  GET /orders/{orderId} HTTP/1.1
```http request

{
	"orderId": 1,
	"orderAt": "2023-05-26",
	"payAmount": 10000, // 최종 결제 금액
	"usedPoint": 3000, // 사용된 포인트
	"savedPoint": 4000, // 적립된 포인트
	"products": [
		{
            "quantity": 5,
            "product": {
                "id": 1,
                "price": 10000,
                "name": "치킨",
                "imageUrl": "http://example.com/chicken.jpg"
            }
        },
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"price": 20000,
				"name": "피자",
				"imageUrl": "http://example.com/pizza.jpg"
			}
        }
    ]
}
```

### 주문 취소
#### DELETE /orders/{orderId} HTTP/1.1
#### HTTP/1.1 200 OK

### 주문 페이지 (응답) -> /point

### 주문 정보 (클라이언트 요청) -> 201 void 형태로 응답
#### POST /orders HTTP/1.1
#### HTTP/1.1 200 OK
#### Content-Type: application/json

```http request
{
	"usedPoint": 3000, // 사용된 포인트
	"products": [
		{
			"productId": 1,
			"quantity": 100
		},
		{
			"productId": 2,
			"quantity": 100
		}
	]
}
```

### 포인트 조회
#### GET /points HTTP/1.1
#### HTTP/1.1 200 OK
#### Content-Type: application/json

```http request
{
	"currentPoint": 50000,
	"toBeExpiredPoint": 1000
}
```

### 예외 처리 응답
```http request
{
	"errorMessage": "잘못된 요청입니다."  // 상황에 맞는 예외 메시지
}
```
