# jwp-shopping-order

## 2단계 추가 API 명세

- 회원가입: POST /users/join
- 로그인: POST /users/login
- 내쿠폰함: GET /users/me/coupons

- 상품주문조회: GET /orders
- 단일주문조회: GET /orders/{orderId}
- 상품주문요청: POST /orders

## 도메인 요구사항

- Member
    - name
    - Cart
    - List<MemberCoupon>
    - List<Order>

- Product
    - name
    - price
    - imageUrl

- Cart
    - List<Item>

- Item
    - Product
    - Quantity

- Order
    - List<Item>
    - Coupon
    - deliveryPrice
    - totalPrice(?)

- Coupon
    - name
    - discountRate
    - period
    - expiredDate

- MemberCoupon
    - Coupon
    - issuedDate
    - expiredDate
