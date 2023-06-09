# jwp-shopping-order

---

### 기능 요구 사항

- [x] 장바구니에 담은 상품을 주문할 수 있다.
    - [x] 상품 주문시 쿠폰을 선택할 수 있다.
    - [x] 배달비는 3천원이다.
- [x] 주문을 성공했다면
    - [x] 쿠폰을 사용했다면 사용한 쿠폰이 제거된다.
    - [x] 주문한 금액이 5만원 이상이면 5천원 할인 쿠폰을 제공한다.
    - [x] 주문한 상품은 장바구니에서 제거된다.
- [x] 다음의 경우 주문을 실패한다.
    - [x] 최종 주문 금액이 음수이면 결제가 실패한다.
    - [x] 적용한 쿠폰의 최소 주문 금액보다 주문한 금액이 적으면 결제가 실패한다.
- [x] 사용자의 쿠폰 목록을 조회할 수 있다.
- [x] 사용자의 주문 목록을 조회할 수 있다.
- [x] 주문 상세 정보를 조회할 수 있다.

### API 명세

<details>
<summary><strong>/products</strong></summary>

**`GET` /products**  
요청

```http request
Request method:    GET

Request URI:	/products
```

응답

```http request
HTTP/1.1 200 
```

```json
[
  {
    "id": 1,
    "name": "치킨",
    "price": 10000,
    "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
  },
  {
    "id": 2,
    "name": "샐러드",
    "price": 20000,
    "imageUrl": "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
  },
  {
    "id": 3,
    "name": "피자",
    "price": 13000,
    "imageUrl": "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"
  }
]
```

<br>

**`GET` /products/{id}**  
요청

```http request
Request method:    GET

Request URI:	/products/1
```

응답

```http request
HTTP/1.1 200 
```

```json
{
  "id": 1,
  "name": "치킨",
  "price": 10000,
  "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
}
```

<br>

**`POST` /products**

요청

```http request
Request method:    POST

Request URI:	/products
```

```json
{
  "name": "햄버거",
  "price": 5000,
  "imageUrl": "https://images.unsplash.com/photo-1561758033-d89a9ad46330?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
}
```

응답

```http request
HTTP/1.1 201

Location: /products/4
```

<br>

**`PUT` /products/{id}**

요청

```http request
Request method:    PUT

Request URI:	/products/1
```

```json
{
  "name": "치킨",
  "price": 20000,
  "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
}
```

응답

```http request
HTTP/1.1 200 
```

<br>

**`DELETE` /products/{id}**  
요청

```http request
Request method:    GET

Request URI:	/products/1
```

응답

```http request
HTTP/1.1 204 
```

</details>

---

<details>
<summary><strong>/cart-items</strong></summary>

**`GET` /cart-items**  
요청

```http request
Request method:    GET

Request URI:	/cart-items
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

응답

```http request
HTTP/1.1 200 
```

```json
[
  {
    "id": 1,
    "quantity": 2,
    "product": {
      "id": 1,
      "name": "치킨",
      "price": 10000,
      "imageUrl": "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
    }
  },
  {
    "id": 2,
    "quantity": 4,
    "product": {
      "id": 2,
      "name": "샐러드",
      "price": 20000,
      "imageUrl": "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
    }
  }
]
```

<br>

**`POST` /cart-items**  
요청

```http request
Request method:    POST

Request URI:	/cart-items
Headers:		Authorization=BasicYUBhLmNvbToxMjM0
```

```json
{
  "productId": 1
}
```

응답

```http request
HTTP/1.1 201

Location: /cart-items/4
```

<br>

**`PATCH` /cart-items/{id}**  
요청

```http request
Request method:    PATCH

Request URI:	/cart-items/1
Headers:		Authorization=BasicYUBhLmNvbToxMjM0
```

```json
{
  "quantity": 2
}
```

응답

```http request
HTTP/1.1 200
```

<br>

**`DELETE` /cart-items/{id}**  
요청

```http request
Request method:    DELETE

Request URI:	/cart-items/1
Headers:		Authorization=BasicYUBhLmNvbToxMjM0
```

응답

```http request
HTTP/1.1 204
```

</details>

---

<details>
<summary><strong>/coupons</strong></summary>

**`GET` /coupons**  
요청

```http request
Request method:    GET

Request URI:	/coupons
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

응답

```http request
HTTP/1.1 200 
```

```json
[
  {
    "id": 1,
    "name": "1000원 할인 쿠폰",
    "discountPrice": 1000
  },
  {
    "id": 3,
    "name": "3000원 할인 쿠폰",
    "discountPrice": 3000
  }
]
```

<br>

**`POST` /coupons**  
요청

```http request
Request method:    POST

Request URI:	/coupons
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

```json
{
  "couponId": 2
}
```

응답

```http request
HTTP/1.1 201

Location: /coupons
```

</details>

---

<details>
<summary><strong>/orders</strong></summary>

**`POST` /orders**  
요청

```http request
Request method:    POST

Request URI:	/orders
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

```json
{
  "cartItemIds": [
    1,
    2
  ],
  "totalPrice": 102000,
  "couponId": 1
}
```

응답 (주문 성공 시)

```http request
HTTP/1.1 200

Location: /orders
```

응답 (주문 실패 시)

```http request
HTTP/1.1 400

Location: /orders
```

<br>

**`GET` /orders**  
요청

```http request
Request method:    GET

Request URI:	/orders
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

응답

```http request
HTTP/1.1 200 
```

```json
[
  {
    "orderId": 1,
    "orderItems": [
      {
        "id": 1,
        "quantity": 3,
        "product": {
          "id": 2,
          "name": "샐러드",
          "price": 20000,
          "imageUrl": "https://images.unsplash.com/photo-2"
        }
      },
      {
        "id": 2,
        "quantity": 2,
        "product": {
          "id": 3,
          "name": "피자",
          "price": 13000,
          "imageUrl": "https://images.unsplash.com/photo-3"
        }
      }
    ]
  },
  {
    "orderId": 2,
    "orderItems": [
      {
        "id": 3,
        "quantity": 1,
        "product": {
          "id": 2,
          "name": "샐러드",
          "price": 20000,
          "imageUrl": "https://images.unsplash.com/photo-2"
        }
      },
      {
        "id": 4,
        "quantity": 1,
        "product": {
          "id": 3,
          "name": "피자",
          "price": 13000,
          "imageUrl": "https://images.unsplash.com/photo-3"
        }
      }
    ]
  }
]
```

<br>

**`GET` /orders/{id}**  
요청

```http request
Request method:    GET

Request URI:	/orders/1
Headers:        Authorization=BasicYUBhLmNvbToxMjM0
```

응답

```http request
HTTP/1.1 200 
```

```json
{
  "order": {
    "orderId": 1,
    "orderItems": [
      {
        "id": 1,
        "quantity": 3,
        "product": {
          "id": 2,
          "name": "샐러드",
          "price": 20000,
          "imageUrl": "https://images.unsplash.com/photo-2"
        }
      },
      {
        "id": 100,
        "quantity": 2,
        "product": {
          "id": 3,
          "name": "피자",
          "price": 13000,
          "imageUrl": "https://images.unsplash.com/photo-3"
        }
      }
    ]
  },
  "totalPrice": 86000
}
```

</details>