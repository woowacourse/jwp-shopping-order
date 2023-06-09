# jwp-shopping-order

## order
- [x] 특정 주문의 상세 정보를 확인할 수 있다. (order.http 주문내역 상세조회)
  - [x] 주문 Id
  - [x] 주문한 product들
    - [x] product
    - [x] 수량
  - [x] 구매 시기
  - [x] 원가
  - [x] 할인가
  - [x] 최종가
- [x] 사용자 별로 주문 목록을 확인할 수 있다. (order.http 전체 주문내역 조회하기)
  - [x] 멤버가 주문한 모든 정보들을 반환한다.
- [x] 장바구니에 담은 상품을 주문할 수 있다.(order.http 주문하기)
  - [x] 주문내역의 id를 응답으로 반환해야한다.
  - [x] 장바구니에서는 삭제 되어야 한다.
  - [x] 주문 내역 테이블에 저장이 되어야한다.
  - [x] 쿠폰을 사용했다면 쿠폰을 사용처리 한다.

## cart-item
  - [x] 장바구니에 아이템을 n 개만큼 담을 수 있다. (cart-item.http product를 quantity만큼 cart에 담는 기능 추가)

## coupon
  - [x] 멤버별로 보유한 쿠폰 목록을 조회할 수 있다.(coupon.http 멤버의 쿠폰 전체 조회하기)
  - [x] 멤버에게 쿠폰을 발급해줄 수 있다.(coupon.http 멤버의 쿠폰 발급받기)
  - [x] 쿠폰을 적용한 할인금액 계산하기.(coupon.http 쿠폰할인 적용)

## products
  - [x] 상품목록을 전체조회할 때 20개단위로 조회하기(products.http 상품목록 page로 가져오기)