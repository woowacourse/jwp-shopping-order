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

* 할인율 조회

```
GET /discount
Request: Query Parameters(price, memberGrade)
Response: [policyName, discountRate, discountPrice]
```

## 할인율

### 회원 등급별

- GOLD 5% 할인
- SILVER 3% 할인
- BRONZE 1% 할인

### 금액 구간별

- 0 - 9,999 => 1% 할인
- 10,000 - 19,999 => 2% 할인
- 20,000 - 29,999 => 3% 할인
- ...
- 100,000 ~ 10% 할인
