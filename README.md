# jwp-shopping-order

## API

### Product API

- [x] 상품 추가 POST `/products` 201
- [x] 전체 상품 조회 GET `/products` 200
- [x] 단일 상품 조회 GET `/products/{productId}` 200
- [x] 상품 수정 PATCH `/products/{productId}` 200
- [x] 상품 삭제 DELETE `/products/{productId}` 204

### Cart Item API

- [x] 장바구니에 아이템 추가 POST `/cart-items` 201
- [x] 장바구니 조회 GET `/cart-items` 200
- [x] 장바구니 아이템 조회 GET `/cart-items/{cartItemId}` 200
- [x] 장바구니 아이템 수정 PATCH `/cart-items/{cartItemId}` 200
- [x] 장바구니 아이템 삭제 DELETE `/cart-items/{cartItemId}` 204

### Order API

- [x] 주문 등록 POST `/orders` 201
- [x] 사용자의 주문 목록 조회 GET `/orders` 200
- [x] 특정 주문의 상세 정보 조회 GET `/orders/{orderId}` 200

### Point API
- [ ] 사용자 포인트 조회 GET `/point` 200

### 할인 정책 정보 API
- [ ] 할인 정책 정보 조회 GET `/order-policy` 200

## 재화 관련 정책

- [ ] 총 주문 금액 3만원 이상 구매시 배송비 무료
- [ ] 포인트를 사용한 후의 주문 금액에 대해 10%의 포인트를 지급

