## 상품

### 상품 목록 조회

Request

```
GET /products HTTP/1.1
```

Response

```
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "name": "치킨",
        "price": 10000,
        "imageUrl": "http://example.com/chicken.jpg"
    },
    {
        "id": 2,
        "name": "피자",
        "price": 20000,
        "imageUrl": "http://example.com/pizza.jpg"
    }
]
```

### 상품 조회
Request
```
GET /products/{productId} HTTP/1.1
```

Response

```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "치킨",
    "price": 10000,
    "imageUrl": "http://example.com/chicken.jpg"
}
```
### 상품 추가

Request

```
POST /products HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```
Response
```
HTTP/1.1 201 Created
Location: /products/{productId}
```

### 상품 수정

Request

```
PUT /products/{productId} HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```
Response

```
HTTP/1.1 200 OK
```

### 상품 삭제

Request

```
DELETE /products/{productId} HTTP/1.1
```

Response
```
HTTP/1.1 204 No Content
```

## 장바구니

### 장바구니 아이템 목록 조회

Request

```
GET /cart-items HTTP/1.1
Authorization: Basic ${credentials}
```

Response

```
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "quantity": 5,
        "product": {
                        "id": 1,
                        "price": 10000,
                        "name": "치킨",
                        "imageUrl": "http://example.com/chicken.jpg"
                    }
    },
    {
        "id": 2,
        "quantity": 1,
        "product": {
                        "id": 2,
                        "price": 20000,
                        "name": "피자",
                        "imageUrl": "http://example.com/pizza.jpg"
                    }
    }
]
```
### 장바구니 아이템 추가
Request
```
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "productId": 1
}
```
Response
```
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```

### 장바구니 아이템 수량 변경
Request
```
PATCH /cart-items/{cartItemId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "quantity": 3
}
```
Response
```
HTTP/1.1 200 OK
```

### 장바구니 아이템 삭제
Request

```
DELETE /cart-items/{cartItemId}
Authorization: Basic ${credentials}
```

Response
```
HTTP/1.1 204 No Content
```

## 포인트

### 사용자가 가진 포인트 조회
Request
```
GET /points HTTP/1.1
Authorization: Basic ${credentials}
```

Response
```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "point" : 300
}
```

### 적립 포인트 조회
Request
```
GET /saving-point?totalPrice={totalPrice} HTTP/1.1
```

Response
```
HTTP/1.1 200 OK
Content-Type: application/json

{
	"savingPoint": 300
}
```

## 주문

### 주문하기
Request
```
POST /orders HTTP/1.1
Content-Type: application/json
Authorization: Basic ${credentials}

{
    "cartItemIds" : [1, 2, 3],
	"cardNumber" : "4043-0304-1299-4949",
	"cvc" : 123,
	"point" : 300
}
```

Response
```
HTTP/1.1 201 Created
Location: /orders/{orderId}
```

### 전체 주문 목록 불러오기
Request
```
GET /orders HTTP/1.1
Authorization: Basic ${credentials}
```

Response
```
HTTP/1.1 200 OK
Content-Type: application/json

[
		[
			"id" : 4,
			"orderedAt" : 2022-03-16T17:40:00+09:00,
			"usedPoint" : 300,
			"savedPoint" : 400,
			"products": [
			        {
	                    "id": 1,
	                    "quantity": 5,
	                    "product": {
	                                    "id": 1,
	                                    "price": 10000,
	                                    "name": "치킨",
	                                    "imageUrl": "http://example.com/chicken.jpg"
	                                }
	                },
	                {
	                    "id": 2,
	                    "quantity": 1,
	                    "product": {
	                                    "id": 2,
	                                    "price": 20000,
	                                    "name": "피자",
	                                    "imageUrl": "http://example.com/pizza.jpg"
	                }
	               }
	    ],
		[
			"id" : 5,
			"orderedAt" : 2022-03-16T17:40:00+09:00,
			"usedPoint" : 300,
			"savedPoint" : 400,
		    "products": {
		        "id": 1,
		        "quantity": 5,
		        "product": {
		            "id": 1,
		            "price": 10000,
		            "name": "치킨",
		            "imageUrl": "http://example.com/chicken.jpg"
		        }
		      },
		      {
		        "id": 2,
		        "quantity": 1,
		        "product": {
		            "id": 2,
		            "price": 20000,
		            "name": "피자",
		            "imageUrl": "http://example.com/pizza.jpg"
		        }
		      }
		]
]
```

### 주문 상세 내역 불러오기
Request
```
GET /orders/{orderId} HTTP/1.1
Authorization: Basic ${credentials}
```

Response
```
HTTP/1.1 200 OK
Content-Type: application/json

[
		"id" : 4,
		"usedPoint" : 300,
		"savedPoint" : 350,
		"orderedAt" : 2022-03-16T17:40:00+09:00,
        "products": [
				{
                    "id": 1,
                    "quantity": 5,
                    "product": {
                              "id": 1,
                              "price": 10000,
                              "name": "치킨",
                              "imageUrl": "http://example.com/chicken.jpg"
	                 }
		        },
		        {
                    "id": 2,
                    "quantity": 1,
                    "product": {
                              "id": 2,
                              "price": 20000,
                              "name": "피자",
                              "imageUrl": "http://example.com/pizza.jpg"
					}
                }
        ]
]
```
