# 요구사항

- [x] 장바구니에 담은 상품을 주문할 수 있다.
- [x] 상품 주문 시 현실 세계의 쇼핑 서비스가 제공하는 재화 관련 요소를 최소 1가지 이상 추가한다.
    - 재화 관련 요소: **포인트**
    - 주문 금액의 10%를 적립한다.
    - 주문 시 사용한 포인트에 대해서는 적립되지 않는다.
    - 주문 시 배송비에 대해서는 적립되지 않는다.
- [x] 사용자 별로 주문 목록을 확인할 수 있다.
- [x] 특정 주문의 상세 정보를 확인할 수 있다.

# API 명세서

## 🙋‍♂️Member API

---

| Method | URL      | HttpStatus | Description   | Header                               |
|--------|----------|------------|---------------|--------------------------------------|
| GET    | /members | 200 (OK)   | 사용자 정보를 조회한다. | Authorization : basic email:password |

### 요청 / 응답

#### GET `/members`

```json
// Response
{
  "id": 1,
  "email": "a@a.com",
  "money": 1000,
  "point": 1000
}
```

### 💰Order API

---

| Method | URL               | HttpStatus    | Description         | Header                               |
|--------|-------------------|---------------|---------------------|--------------------------------------|
| GET    | /orders           | 200 (OK)      | 사용자의 주문을 조회한다.      | Authorization : basic email:password |
| POST   | /orders           | 201 (CREATED) | 사용자가 주문한다.          | Authorization : basic email:password |
| GET    | /orders/{orderId} | 200 (OK)      | 해당 주문의 상세 정보를 조회한다. | Authorization : basic email:password |

### 요청 / 응답

#### GET `/orders`

```json
// Response
[
  {
    "orderId": 1,
    "orderProducts": [
      {
        "productId": 24,
        "name": "친환경 실링 용기",
        "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
        "quantity": 3,
        "price": 60000,
        "totalPrice": 180000
      },
      {
        "productId": 25,
        "name": "친환경 실링 용기222",
        "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
        "quantity": 1,
        "price": 50000,
        "totalPrice": 50000
      }
    ]
  },
  {
    "orderId": 2,
    "orderProducts": [
      {
        "productId": 33,
        "name": "아이템 2",
        "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
        "quantity": 3,
        "price": 1000,
        "totalPrice": 3000
      },
      {
        "productId": 55,
        "name": "아이템 5",
        "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
        "quantity": 2,
        "price": 14000,
        "totalPrice": 28000
      }
    ]
  }
]
```

#### POST `/orders`

```json
// Request
{
  "cartIds": [
    1,
    2,
    3
  ],
  // 장바구니_상품 식별자값
  "point": 1000
  // 포인트
}
```

#### GET `/orders/{orderId}`

```json
// Response
{
  "orderId": 1,
  "orderProducts": [
    {
      "productId": 33,
      "name": "아이템 2",
      "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
      "quantity": 3,
      "price": 1000,
      "totalPrice": 3000
    },
    {
      "productId": 55,
      "name": "아이템 5",
      "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300",
      "quantity": 2,
      "price": 14000,
      "totalPrice": 28000
    }
  ],
  "orderTotalPrice": 31000,
  "usedPoint": 200,
  "createdAt": "2023-05-26 21:00:01"
}
```

## 🧺CartItem API

---

| Method | URL                   | HttpStatus       | Description                  | Header                               |
|--------|-----------------------|------------------|------------------------------|--------------------------------------|
| GET    | /cart-items/          | 200 (OK)         | 장바구니를 모두 조회한다.               | Authorization : basic email:password |
| POST   | /cart-items/          | 201 (CREATED)    | 장바구니에 상품을 추가한다.              | Authorization : basic email:password |
| PATCH  | /cart-items/{cart-id} | 200 (OK)         | {cart-id} 장바구니의 상품 개수를 수정한다. | Authorization : basic email:password |
| DELETE | /cart-items/{cart-id} | 204 (NO_CONTENT) | {cart-id} 장바구니의 상품을 삭제한다.    | Authorization : basic email:password |

### 요청 / 응답

#### GET `/cart-items`

```json
// Response
[
  {
    "id": 1,
    "quantity": 10,
    "product": {
      "id": 1,
      "name": "PET보틀-정사각(420ml)",
      "price": 43400,
      "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300"
    }
  },
  {
    "id": 2,
    "quantity": 1,
    "product": {
      "id": 2,
      "name": "PET보틀-밀크티(370ml)",
      "price": 73400,
      "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/ac90cb6d-70ad-4271-a25e-03e4db9a9960.jpg?h=300&w=300"
    }
  }
]
```

#### POST `/cart-items`

```json
// Request
{
  "productId": 0
}
```

#### PATCH `/cart-items`

```json
// Requset
{
  "quantity": 0
}
```

## ✋Product API

---

| Method | URL            | HttpStatus       | Description     |
|--------|----------------|------------------|-----------------|
| GET    | /products      | 200 (OK)         | 상품을 모두 조회한다.    |
| GET    | /products/{id} | 200 (OK)         | 하나의 상품을 조회한다.   |
| POST   | /products      | 201 (CREATED)    | 상품을 생성한다.       |
| PUT    | /products/{id} | 200 (OK)         | 해당 상품 정보를 수정한다. |
| DELETE | /products/{id} | 204 (NO_CONTENT) | 해당 상품을 삭제한다.    |

### 요청 / 응답

#### GET `/products`

```json
// Response
[
  {
    "id": 1,
    "name": "PET보틀-정사각(420ml)",
    "price": 43400,
    "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300"
  },
  {
    "id": 2,
    "name": "PET보틀-밀크티(370ml)",
    "price": 73400,
    "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/ac90cb6d-70ad-4271-a25e-03e4db9a9960.jpg?h=300&w=300"
  }
]
```

#### GET `/products/{id}`

```json
// Response
{
  "id": 1,
  "name": "PET보틀-정사각(420ml)",
  "price": 43400,
  "imageUrl": "https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300"
}
```

#### POST `/products`

```json
// Request
{
  "name": "상품명",
  "price": 1000,
  "imageUrl": "https://i.namu.wiki/i/pTVoWDp5G09PGTRUTbCy8raXo9CB47uF2wcuzdUYTlPwRjU6zjl0Reoih4MIXXRTnfxVl-yKlPjTQSVhAbfSxA.webp"
}
```

#### PUT `/products`

```json
// Request
{
  "name": "상품명",
  "price": 1000,
  "imageUrl": "https://i.namu.wiki/i/pTVoWDp5G09PGTRUTbCy8raXo9CB47uF2wcuzdUYTlPwRjU6zjl0Reoih4MIXXRTnfxVl-yKlPjTQSVhAbfSxA.webp"
}
```
