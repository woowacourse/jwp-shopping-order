## 사용자

### 현재 로그인 된 사용자 정보 조회

* 현재 로그인된 사용자의 정보를 조회합니다.
* 현재는 반환 정보가 적립한 포인트밖에 없습니다.

```
GET /profile
Authorization: Basic ${credentials}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "points": 1320
}
```

## 상품

### 상품 목록 조회

```
GET /products HTTP/1.1
```

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

```
GET /products/{productId} HTTP/1.1
```

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

```
POST /products HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```

```
HTTP/1.1 201 Created
Location: /products/{productId}
```

### 상품 수정

```
PUT /products/{productId} HTTP/1.1
Content-Type: application/json

{
  "name": "부리또",
  "price": 30000,
  "imageUrl": "http://example.com/burrito.jpg"
}
```

```
HTTP/1.1 200 OK
```

### 상품 삭제

```
DELETE /products/{productId} HTTP/1.1
```

```
HTTP/1.1 204 No Content
```

## 장바구니

### 장바구니 아이템 목록 조회

```
GET /cart-items HTTP/1.1
Authorization: Basic ${credentials}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "quantity": 5,
    "checked": true,
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
    "checked": false,
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

```
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
  "productId": 1
}
```

```
HTTP/1.1 201 Created
Content-Type: application/json
Location: /cart-items/{cartItemId}

{
  "quantity": 1,
  "checked": true
}
```

### 장바구니 아이템 수량 변경

```
PATCH /cart-items/{cartItemId}
Authorization: Basic ${credentials}
Content-Type: application/json

{
  "quantity": 3,
  "checked": true
}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "quantity": 3,
  "checked": true
}
```

### 장바구니 아이템 삭제

```
DELETE /cart-items/{cartItemId}
Authorization: Basic ${credentials}
```

```
HTTP/1.1 204 No Content
```

## 포인트

* 주문할 때 포인트를 사용한 부분은 제하고 10%를 적립합니다.
* 10원 단위로만 사용할 수 있습니다. (ex. 27원 사용 불가)
* 포인트 정책은 백엔드에서 결정하기 때문에 반드시 포인트 계산 결과를 백엔드에서 가져와서 사용하여야 합니다.

### 현재 장바구니에서 얻을 수 있는 포인트 조회

* `savingRate`는 포인트 적립 비율을 의미합니다. 만약 17000원 주문을 생성한다면, 1700원이 적립됩니다.
* `points`는 현재 장바구니에 담긴 물품을 결제할 시 얻을 수 있는 포인트입니다.

```
GET /cart-points
Authorization: Basic ${credentials}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "savingRate": 10,
  "points": 1270
}
```

## 주문

### 주문 조회

```
GET /orders
Authorization: Basic ${credentials}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "savingRate": 10,
    "points": 420,
    "cartItems": [
      {
        "productId": 5,
        "name": "새우깡",
        "price": 1200,
        "quantity": 1,
        "imageUrl": "http://example.com/products/5.png"
      },
      {
        "productId": 7,
        "name": "바나나킥",
        "price": 1000,
        "quantity": 3,
        "imageUrl": "http://example.com/products/7.png"
      }
    ]
  }
]
```

### 단일 주문 조회

```
GET /orders/{orderId}
Authorization: Basic ${credentials}
```

```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "savingRate": 10,
  "points": 420,
  "cartItems": [
    {
      "productId": 5,
      "name": "새우깡",
      "price": 1200,
      "quantity": 1,
      "imageUrl": "http://example.com/products/5.png"
    },
    {
      "productId": 7,
      "name": "바나나킥",
      "price": 1000,
      "quantity": 3,
      "imageUrl": "http://example.com/products/7.png"
    }
  ]
}
```

### 주문 생성

* `usedPoints`는 주문 시 사용할 포인트입니다.

```
POST /orders
Authorization: Basic ${credentials}
Content-Type: application/json

{
  "usedPoints": 1170,
  "cartItems": [
    {
      "id": 3,
      "productId": 5,
      "quantity": 8
    },
    {
      "id": 4,
      "productId": 7,
      "quantity": 1
    }
  ]
}
```

```
HTTP/1.1 201 Created
Location: /orders/{orderId}
```
