# jwp-shopping-order

## 추가된 요구사항

- 재화는 포인트 선택
- 총 상품 금액의 10%가 적립된다.
- ex. 총 상품 금액 10000원 배송비 3000원의 경우 포인트 1000원이 적립된다.
- 5만원 이상 시 무료, 5만원 미만은 3000원의 배송비가 추가된다.

## API 명세

### 상품 API

#### 상품 목록 조회

- Request

```http request
GET /products HTTP/1.1
```

- Response

```http request
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

#### 상품 조회

- Request

```http request
GET /products/{productId} HTTP/1.1
```

- Response

```http request 
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "치킨",
    "price": 10000,
    "imageUrl": "http://example.com/chicken.jpg"
}

```

#### 상품 추가

- Request

```http request
POST /products HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```

- Response

```http request
HTTP/1.1 201 Created
Location: /products/{productId}
```

#### 상품 수정

- Request

```http request
PUT /products/{productId} HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```

- Response

```http request
HTTP/1.1 200 OK
```

#### 상품 삭제

- Request

```http request
DELETE /products/{productId} HTTP/1.1
```

- Response

```http request
HTTP/1.1 204 No Content
```

### 장바구니 API

#### 장바구니 아이템 목록 조회

- Request

```http request
GET /cart-items HTTP/1.1
```

- Response

```http request
Authorization: Basic ${credentials}
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

#### 장바구니 아이템 추가

- Request

```http request
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
"productId": 1
}
```

- Response

```http request
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```

#### 장바구니 아이템 수량 변경

- Request

```http request
PATCH /cart-items/{cartItemId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
"quantity": 3
}
```

- Response

```http request
HTTP/1.1 200 OK
```

#### 장바구니 아이템 삭제

- Request

```http request
DELETE /cart-items/{cartItemId}
Authorization: Basic ${credentials}
```

- Response

```http request
HTTP/1.1 204 No Content
```

#### 장바구니 여러 아이템 삭제

- Request

```http request
DELETE /cart-items
Authorization: Basic ${credentials}

{
    "cartItemIds": [1, 2, 3, 4]
}

```

- Response

```http request
HTTP/1.1 204 No Content
```

### 주문 API

#### 주문하기

- Request

```http request
POST /orders
Authorization: Basic ${credentials}

{
    "cartItemIds" : [1, 2, 3, 4],
    "point" : 2000
}
```

- Response

```http request
HTTP/1.1 201 Created
Location: /orders/{orderId}
```

#### 유저의 단일 주문 조회

- Request

```http request
GET /orders
Authorization: Basic ${credentials}
```

- Response

```http request
HTTP/1.1 200 OK

{
    "orderId" : 1,
    "products" : [
        {
            "productId": 2
            "name": "치킨"
            "price": 20000
            "imageUrl": "https://example.com/chicken.png"
            "quantity": 2
        }
    ],
    "totalPrice" : 40000
    "usedPoint" : 2000
    "deliveryFee" : 3000
    "orderedAt" : 2023/05/26 02:25:36
}
```

#### 유저의 모든 주문 조회

- Request

```http request
GET /orders/{orderId}
Authorization: Basic ${credentials}
```

- Response

```http request
HTTP/1.1 200 OK

[
   {
        "orderId" : 1,
        "products" : [
            {
                "productId": 2
                "name": "치킨"
                "price": 20000
                "imageUrl": "https://example.com/chicken.png"
                "quantity": 2
            }
        ],
        "totalPrice" : 40000
        "usedPoint" : 2000
        "deliveryFee" : 3000
        "orderedAt" : 2023/05/26 02:25:36
    }
]
```

### 포인트 API

#### 멤버 포인트 조회

- Request

```http request
GET /members/point
Authorization: Basic ${credentials}
```

- Response

```http request
HTTP/1.1 200 OK

{
    "point": 1000
}
```
