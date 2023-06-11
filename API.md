# API

### 사용 안내

사용자 인증/인가
API에서 사용자의 신원을 확인하고 리소스 접근에 대한 허용이 필요한 경우 Basic 인증을 사용합니다.

```http request
Authorization: Basic ${credentials}
```

`credentials`은 `{email}:{password}` 값을 Base64로 인코딩한 값 입니다.

예를 들어, email이 `a@a.com` password가 `password1` 인 사용자의 경우, 요청시 아래와 같은 HTTP Header를 설정합니다.

```http request
Authorization: Basic YUBhLmNvbTpwYXNzd29yZDE=
```

---

## 상품 API

### 상품 목록 조회

`Request`

```http request
GET /products HTTP/1.1
```

`Response`

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

### 상품 조회

`Request`

```http request
GET /products/{productId} HTTP/1.1
```

`Response`

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

### 상품 추가

`Reqeust`

```http request
POST /products HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```

`Response`

```http request
HTTP/1.1 201 Created
Location: /products/{productId}
```

### 상품 수정

`Reqeust`

```http request
PUT /products/{productId} HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```

`Response`

```http request
HTTP/1.1 200 OK
```

### 상품 삭제

`Request`

```http request
DELETE /products/{productId} HTTP/1.1
```

`Response`

```http request
HTTP/1.1 204 No Content
```

---

## 장바구니 API

### 장바구니 아이템 목록 조회

`Request`

```http request
GET /cart-items HTTP/1.1
Authorization: Basic ${credentials}
```

`Response`

```http request
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

`Request`

```http request
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "productId": 1
}
```

`Response`

```http request
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```

### 장바구니 아이템 수량 변경

`Request`

```http request
PATCH /cart-items/{cartItemId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "quantity": 3
}
```

`Response`

```http request
HTTP/1.1 200 OK
```

### 장바구니 아이템 삭제

`Request`

```http request
DELETE /cart-items/{cartItemId}
Authorization: Basic ${credentials}
```

`Response`

```http request
HTTP/1.1 204 No Content
```

---

## 주문 API

### 상품 주문

`Request`

```http request
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

`Response`

```http request
HTTP/1.1 201 Created
Location: /orders/{orderId}
```

### 전체 주문 목록 조회

`Request`

```http request
GET /orders HTTP/1.1
Authorization: Basic ${credentials}
```

`Response`

```http request
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "orderedAt": "2023-06-05T11:11:27",
        "usedPoint": 1000,
        "savedPoint": 1000,
        "products": [
            {
                "id": 1,
                "quantity": 2,
                "products": {
                    "id": 1,
                    "name": "치킨",
                    "price": 10000,
                    "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
                }
            },
            {
                "id": 2,
                "quantity": 4,
                "products": {
                    "id": 2,
                    "name": "샐러드",
                    "price": 20000,
                    "imageUrl": "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
                }
            }
        ]
    },
    {
        "id": 2,
        "orderedAt": "2023-06-05T11:11:46",
        "usedPoint": 1000,
        "savedPoint": 130,
        "products": [
            {
                "id": 3,
                "quantity": 1,
                "products": {
                    "id": 3,
                    "name": "피자",
                    "price": 13000,
                    "imageUrl": "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"
                }
            }
        ]
    }
]
```

### 주문 상세 조회

`Request`

```http request
GET /orders/{orderId} HTTP/1.1
Authorization: Basic ${credentials}
```

`Response`

```http request
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "orderedAt": "2023-06-05T01:02:11",
    "usedPoint": 300,
    "savedPoint": 1000,
    "products": [
        {
            "id": 1,
            "quantity": 2,
            "products": {
                "id": 1,
                "name": "치킨",
                "price": 10000,
                "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
            }
        },
        {
            "id": 2,
            "quantity": 4,
            "products": {
                "id": 2,
                "name": "샐러드",
                "price": 20000,
                "imageUrl": "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
            }
        }
    ]
}
```

---

## 포인트 API

### 사용자가 가진 포인트 조회

`Request`

```http request
GET /points HTTP/1.1
Authorization: Basic ${credentials}
```

`Response`

```http request
HTTP/1.1 200 OK
Content-Type: application/json

{
    "point" : 300
}
```

### 적립되는 포인트 조회

`Request`

```http request
GET /saving-point?totalPrice={totalPrice} HTTP/1.1
```

`Response`

```http request
HTTP/1.1 200 OK
Content-Type: application/json

{
    "savingPoint": 300
}
```
