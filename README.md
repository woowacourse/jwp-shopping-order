# 장바구니 - 협업

## 쿠폰 정책

금액 할인

1. ~이상 주문 시 적용 가능
2. 쿠폰에 정해진 액수만큼 할인
3. 사용자 별로 쿠폰 관리

## 백엔드 요구사항

- [x]  전체 쿠폰 반환한다.
- [x]  사용자 별로 쿠폰 발급한다(하나의 쿠폰은 사용자 별로 한번씩 사용 가능하다.)
- [x]  사용자 별 사용 가능한 쿠폰 반환한다.
- [x]  장바구니에 상품을 주문한다
    - [x]  쿠폰을 적용한다.
        - [x]  쿠폰 사용 표시한다.
    - [x]  금액을 계산한다.
- [x]  사용자 별 주문 목록
- [x]  주문 별 상세 페이지

---

## **쿠폰 API**

### **사용자별 전체 쿠폰 조회**

- Request

```json
GET /coupons HTTP/1.1
Host: localhost: 8080
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

### **사용자별 쿠폰 발급**

- Request

```json
POST /coupons/{
  id
} HTTP/1.1
Host: localhost: 8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...

```

- Response

```json
HTTP/1.1 201 Created
```

### **사용자가 사용 가능한 쿠폰 반환**

- Request

```json
GET /coupons/active?total=100000 HTTP/1.1
Host: localhost: 8080
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
  ...
]

```

### **쿠폰 할인 금액 조회**

- Request

```json
GET /coupons/{
  id
}/discount?total=30000 HTTP/1.1
Host: localhost: 8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

{
  "discountedProductAmount": 27000,
  "discountAmount": 3000
}

```

---

## **주문 API**

### **주문 요청**

- Request

```json
POST /orders HTTP/1.1
Host: localhost: 8080
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
  "discountedProductAmount": 3000,
  "address": "서울특별시 송파구 ..."
}

```

### **단일 주문 상세 조회**

- Request

```json
GET /orders/{
  id
} HTTP/1.1
Host: localhost: 8080
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

### **사용자별 전체 주문 조회**

- Request

```json
GET /orders HTTP/1.1
Host: localhost: 8080
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

## **수정 사항 반영 API**

### **장바구니 상품 추가**

Request

```json
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}

{
"productId": 1,
"quantity": 5
}

```

Response

```json
HTTP/1.1 201 Created
Location: /cart-items/{
cartItemId
}

```

### **장바구니 상품 삭제 (여러 개)**

Request

```json
DELETE /cart-items
Authorization: Basic ${credentials}

{
"cartItemIds": [1, 3, 5]
}

```

Response

```json
HTTP/1.1 204 No Content
```
