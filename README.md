# 장바구니 - 협업

## 쿠폰 정책

1. 금액 할인
    1. ~이상 주문 시 적용 가능
    2. 쿠폰에 정해진 액수만큼 할인
    3. 사용자 별로 쿠폰 관리
2. 퍼센트 할인
    1. ~이상 주문 시 적용 가능
    2. 정해진 퍼센트만큼 할인
    3. 사용자 별로 쿠폰 관리
3. 배송비 할인
    1. 배송비 무료

## 백엔드 요구사항

- [ ]  전체 쿠폰 반환한다.
- [ ]  사용자 별로 쿠폰 발급한다(하나의 쿠폰은 사용자 별로 한번씩 사용 가능하다.)
- [ ]  사용자 별 사용 가능한 쿠폰 반환한다.
- [ ]  장바구니에 상품을 주문한다
    - [ ]  쿠폰을 적용한다.
        - [ ]  쿠폰 사용 표시한다.
    - [ ]  금액을 계산한다.
- [ ]  사용자 별 주문 목록
- [ ]  주문 별 상세 페이지

---

## 쿠폰

### 사용자별 전체 쿠폰 조회

- Request

```java
GET/coupons HTTP/1.1
    Host:localhost:8080
    Authorization:Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

[
  {
    "couponId": 1,
    "couponName": "오늘만 10%할인 쿠폰",
    "minAmount": "15000",
    "isPublished": false
  },
  {
    "couponId": 2,
    "couponName": "사장님이 미쳤어요! 99%할인 쿠폰",
    "minAmount": "999999999",
    "isPublished": true
  },
  ...
]
```

### 쿠폰 발급

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

### 사용자가 사용 가능한 쿠폰 반환

- Request

```java
GET/coupons/active?total=100000HTTP/1.1
    Host:localhost:8080
    Authorization:Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
```

- Response

```json
HTTP/1.1 200 OK

[
  {
    "couponId": 1,
    "couponName": "오늘만 10%할인 쿠폰",
    "minAmount": "15000"
  },
  {
    "couponId": 2,
    "couponName": "사장님이 미쳤어요! 99%할인 쿠폰",
    "minAmount": "9999"
  },
  ...
]
```

### 쿠폰 할인 금액 조회

- Request

```json
GET /coupons/discount HTTP/1.1
Host: localhost: 8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...

{
"totalAmount": 30000,
"deliveryAmount": 2000,
"couponId": 1
}
```

- Response

```json
HTTP/1.1 200 OK

{
  "totalAmount": 27000,
  "deliveryAmount": 2000
}

```

---

## 주문

### 주문 요청

- Request

```json
POST /orders HTTP/1.1
Host: localhost: 8080
Authorization: Basic bWFuZ29Ad29vdGVjby5jb206bWFuZ29wYXNzd29yZA==
...

{
"productIds": [1, 2, 5], // 상품에 대한 더 자세한 정보는 줄 필요 없을 것 같음
"totalAmount": 30000, // 배달비를 포함하지 않은 상품 금액만 전부 더한 금액?
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
  "total_amount": 55000,
  // 배달비를 포함하지 않은 상품 금액만 전부 더한 금액?
  "delivery_amount": 2000,
  "discounted_amount": 3000,
  // 할인 금액을 할 건지 or 할인된 총 금액을 할 건지
  "address": "서울특별시 송파구 ..."
}
```

### 단일 주문 상세 조회

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
  "total_amount": 55000,
  // 배달비를 포함하지 않은 상품 금액만 전부 더한 금액?
  "delivery_amount": 2000,
  "discounted_amount": 53000,
  //할인된 총 금액을 할 건지
  "address": "서울특별시 송파구 ..."
}
```

### 사용자별 전체 주문 조회

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

    // 여기에 추가로 총 상품 금액 등을 넣어도 되지만, Figma에 나와있는 시안대로만 했음
  },
  {
    "id": 2,
    ...
  }
  ...
]
```

## 수정 사항 반영

### 장바구니 상품 추가

Request

```
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
   "productId": 1,
   "quantity": 5
}
```

Response

```
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```

### 장바구니 상품 삭제 (여러 개)

Request

```
DELETE /cart-items
Authorization: Basic ${credentials}

{
   "cartItemIds": [1, 3, 5]
}
```

Response

```
HTTP/1.1 204 No Content
```

## 고민

- 상품 주문 중 금액이 바뀔 경우 어떻게 대처해야 되는가?
