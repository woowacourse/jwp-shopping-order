# API 명세

* 장바구니의 상품 주문

```
POST /orders
Request: [cartItemId], Authorization
Response: Created /orders/{order_id}
```

* 사용자별 주문 목록 조회

```
GET /orders
Request: Authorization
Response: [orderId, CartItemResponse[id, quantity, ProductResponse(id, name, price, imageUrl)]]
```

* 특정 주문 상세 정보 조회

```
GET /orders/{id}
Request: Authorization
Response: orderId, CartItemResponse[id, quantity, ProductResponse(id, name, price, imageUrl)]
```

* 금액에 따른 할인율 조회

```
미정
```

* 등급에 따른 할인율 조회

```
미정
```
