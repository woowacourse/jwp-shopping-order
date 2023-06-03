# jwp-shopping-order

## 2단계 추가 API 명세

- 회원가입: POST /users/join
- 로그인: POST /users/login
- 내쿠폰함: GET /users/me/coupons

- 상품주문조회: GET /orders
- 단일주문조회: GET /orders/{orderId}
- 상품주문요청: POST /orders

## 기능 요구사항

- 사용자(Member): name, password
    - [x] 사용자 이름은 4글자 이상 10글자 이하만 가능하다.
    - [x] 사용자 이름은 중복될 수 없다.
    - [x] 사용자 비밀번호는 4글자 이상 10글자 이하만 가능하다.
    - [x] DB에 저장할 때는 SHA-256으로 암호화된 값을 저장한다.


- 상품(Product): name, price, imageUrl
    - [x] 상품 이름은 1글자 이상 20글자 이하만 가능하다.
    - [x] 상품 가격은 1원부터 10,000,000원까지 가능하다.


- 쿠폰(Coupon): name, discountRate, period, expiredAt
    - [x] 쿠폰 이름은 1글자 이상 50글자 이하만 가능하다.
    - [x] 쿠폰 할인율은 5%부터 90%까지 가능하다.
    - [x] 쿠폰 기간은 1일부터 365일까지 가능하다.


- 사용자가 발급 받은 쿠폰 (MemberCoupon): Coupon, issuedAt, expiredAt
    - [x] 사용자가 처음 가입했을 때, 신규 가입 축하 할인 쿠폰을 지급한다.
    - [ ] 사용자가 첫 주문을 했을 때, 첫 주문 할인 쿠폰을 지급한다.
    - [ ] 사용자는 동일한 쿠폰을 중복해서 받을 수 없다.
    - [ ] 멤버의 쿠폰 만료일은 min(쿠폰의 만료날짜, 멤버의 쿠폰 발급 날짜 + period)이다.


- 아이템 (Item): Product, Quantity
    - [x] 한 상품의 개수는 최대 1000개까지만 가능하다.


- 장바구니 (Cart): Member, Item


- 주문정보 (Order): Member, List<Item>, Coupon, deliveryPrice, totalPrice, discountedTotalPrice
    - [ ] 장바구니에 있는 상품을 주문 했을 경우, 장바구니에서 상품이 삭제된다.
    - [x] 주문 시, 총 상품의 개수는 최대 1000개까지만 가능하다.
