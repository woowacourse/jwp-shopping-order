# jwp-shopping-order 요구사항

- [x] 쿠폰 할인 금액 조회
- [x] 사용자별 전체 쿠폰 조회
- [x] 사용자별 쿠폰 발급
- [x] 사용자가 사용 가능한 쿠폰 반환
- [x] 주문 요청
  - [ ] 쿠폰 적용하지 않을 때도 추가 고려
- [x] 단일 주문 상세 조회 
- [x] 사용자별 전체 주문 조회
- [ ] 기본 API 수정
    - [ ] 장바구니 상품 추가(여러개 가능)
    - [ ] 장바구니 선택 삭제(여러개 가능)
    - [ ] 장바구니에 있던 장품 추가하면 개수만 추가되도록 수정

## 쿠폰 API

### 사용자별 전체 쿠폰 조회
- Request

```json
GET /coupons HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

[
  {
    "couponId": 1,
    "couponName": "1000원 할인 쿠폰",
    "minAmount": 15000,
    "discountAmount": 1000,
    "isPublished": false
  },
  {
    "couponId": 2,
    "couponName": "사장님이 미쳤어요! 99999원 할인 쿠폰",
    "minAmount": 999999999,
    "discountAmount": 99999,
    "isPublished": true
  },
  ...
]

```

### 사용자별 쿠폰 발급

- Request

```json
POST /coupons/{id} HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...
```

- Response 

```json
HTTP/1.1 201 Created
```

### 사용자가 사용 가능한 쿠폰 반환

- Request

```json
GET /coupons/active?total=100000 HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

[
  {
    "couponId": 1,
    "couponName": "1000원 할인 쿠폰",
    "minAmount": 15000
  },
  {
    "couponId": 2,
    "couponName": "사장님이 미쳤어요! 99999원 할인 쿠폰",
    "minAmount": 999999999
  },
  ...
]
```

### 쿠폰 할인 금액 조회

- Request

```json
GET /coupons/{id}/discount?total=30000 HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

{
  "discountedProductAmount": 27000
}
```

---

## 주문 API

### 주문 요청

- Request

```json
POST /orders HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...

{
    "products": [
    {
    "id": 1,
    "quantity": 3,
    },
    {
    "id": 2,
    "quantity": 1,
    },
    ]
    "totalProductAmount": 30000,
    "deliveryAmount": 2000,
    "address": "서울특별시 송파구 ...",
    "couponId": 1
}
```

- Response

```json
HTTP/1.1 201 Created

{
  "id": 1,
  "products": [
    {
      "id": 1,
      "name": "치킨",
      "imgUrl": "image.jpeg",
      "price": 15000,
      "quantity": 1
    },
    {
      "id": 3,
      "name": "피자",
      "imgUrl": "image.jpeg",
      "price": 20000,
      "quantity": 2
    }
  ],
  "totalProductAmount": 55000,
  "deliveryAmount": 2000,
  "discountedProductAmount": 52000,
  "address": "서울특별시 송파구 ..."
}
```

### 단일 주문 상세 조회

- Request

```json
GET /orders/{id} HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...
```

- Response

```json
HTTP/1.1 200 OK
...

{
  "id": 1,
  "products": [
    {
      "id": 1,
      "name": "치킨",
      "imgUrl": "image.jpeg",
      "price": 15000,
      "quantity": 1
    },
    {
      "id": 3,
      "name": "피자",
      "imgUrl": "image.jpeg",
      "price": 20000,
      "quantity": 2
    }
  ],
  "totalProductAmount": 55000,
  "deliveryAmount": 2000,
  "discountedProductAmount": 53000,
  "address": "서울특별시 송파구 ..."
}
```

### 사용자별 전체 주문 조회

- Request

```json
GET /orders HTTP/1.1
Host: localhost:8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...
```

- Response

```json
HTTP/1.1 200 OK
...

[
  {
    "id": 1,
    "products": [
      {
        "id": 1,
        "name": "치킨",
        "imgUrl": "image.jpeg",
        "price": 15000,
        "quantity": 1
      },
      {
        "id": 3,
        "name": "피자",
        "imgUrl": "image.jpeg",
        "price": 20000,
        "quantity": 2
      }
    ]
  },
  {
    "id": 2,
    ...
  }
  ...
]
```

---

수정 사항 반영 API
- 장바구니 상품 추가
- 장바구니 상품 삭제 (여러 개)

## 수정 사항 반영 API

### 장바구니 상품 추가

- Request

```json
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}

{
   "productId": 1,
   "quantity": 5
}
```

- Response

```json
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```

### 장바구니 상품 삭제 (여러 개)

- Request

```json
DELETE /cart-items
Authorization: Basic ${credentials}

{
   "cartItemIds": [1, 3, 5]
}
```

- Response

```json
DELETE /cart-items
Authorization: Basic ${credentials}

{
   "cartItemIds": [1, 3, 5]
}
```
