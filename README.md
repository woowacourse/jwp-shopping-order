# jwp-shopping-order

## 기능 목록

- 사용자는 자신의 장바구니의 물건을 주문할 수 있다.
    - 주문한 장바구니 물거는 주문 이후 삭제된다.
- 사용자는 장바구니의 물건을 주문할 때 포인트를 사용할 수 있다.
    - 실제 사용한 금액 (전체 금액 - 포인트 사용금액)에 대한 2.5% 포인트가 누적된다.
- 사용자 포인트를 조회할 수 있다.
- 사용자가 특정 주문에 얼마만큼의 포인트가 누적되었는지 조회할 수 있다.

## API 명세

### ✔ 구매

### Request

```
POST /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
	"cartItemIds":[1,2,3,4],
	"usePoint":1000
}
```

### Response

```
HTTP/1.1 201 Created
Content-Type: application/json
Location: orders/{id}
```

### ✔ 전체 주문 목록 조회

### Request

```
GET /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
				"id": 2,             
				"price": 50000,      
				"orderDate": 2023-05-26T12:34
				"orders" : [    
	        { "id": 1,          
	          "quantity": 5,    
	           "price": 20000,  
	           "name": "피자",    
	           "imageUrl": "http://example.com/pizza.jpg"
	        },
					{ "id": 2,
	          "quantity": 5,
	           "price": 20000,
	           "name": "피자2",
	           "imageUrl": "http://example.com/pizza2.jpg"
	        }
				]
    },
]
```

### ✔ 단일 주문 조회

### Request

```
GET /orders/{orderId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```
HTTP/1.1 200 OK
Content-Type: application/json
{
        "id": 2,              
        "price": 50000,       
        "orderDate": "2023-05-26T12:34",
        "orders" : [          
	        { "id": 1,          
	          "quantity": 5,    
	           "price": 20000,  
	           "name": "피자",    
	           "imageUrl": "http://example.com/pizza.jpg" 
	        },
					{ "id": 2,
	          "quantity": 5,
	           "price": 20000,
	           "name": "피자2",
	           "imageUrl": "http://example.com/pizza2.jpg"
	        }
	        ]
}
```

### 포인트 API

### ✔ 회원이 가지고 있는 전체 포인트 조회

### Request

```
GET /points HTTP/1.1
Authorization: Basic ${
credentials
}
Content-Type: application/json
```

### Response

```
HTTP/1.1 200 Ok
Content-Type: application/json

{
	"points":10
}
```

### ✔️주문으로 적립된 포인트 조회

### Request

```
GET orders/{id}/points HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```
HTTP/1.1 200 Ok
Content-Type: application/json

{
	"points_saved":10
}
```

## 에러 명세

### 상품

- 없는 상품에 대해서
    - 조회
    - 수정
    - 삭제

⛔ status: 404 (*NOT_FOUND*)

### 장바구니 목록

- 다른 유저의 장바구니 목록을
    - 조회
    - 수량 변경
    - 삭제

⛔ status: 403 (*FORBIDDEN*)

### 주문

- 다른 유저의 장바구니 목록을
    - 주문
- 다른 유저의 주문을
    - 조회

⛔ status: 403 (*FORBIDDEN*)

- 장바구니에 없는 목록을 (장바구니 id가 없는 경우)
    - 주문

⛔ status: 404 (*NOT_FOUND*)

### 포인트

- 상품 구매 가격보다 더 많은 포인트를 사용할 때
- 포인트가 0보다 작을 때
- 자기가 보유한 포인트보다 더 많은 포인트를 사용할 때

⛔ status: 400 (*BAD_REQUEST*)
