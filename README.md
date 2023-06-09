# jwp-shopping-order

[노션 명세서 API]("https://wooteco-ash.notion.site/ee9a0d03d81e429c9c98f7a573f9e252?v=6023debb4969410eb3102fad120b7d06")

# 주문

## 선택한 장바구니에 대하여 회원의 쿠폰 내역 조회

- [x] cartItemId List를 입력받아, 사용자가 소유한 쿠폰을 토대로 해당 주문에 쿠폰을 적용한 가격을 반환한다.
- [x] 이 때, 적용 불가능(최소적용금액보다 주문금액이 적은 경우)한 쿠폰도 함께 반환한다.
    - [x] 이 경우 discountPrice는 null로 반환한다.

## 장바구니 목록을 통해 주문

- [x] cartItemId List를 통해 주문을 진행한다.
- [x] 적용할 couponId을 함께 입력받는다.
    - [x] 최대 1장 적용, 없으면 null
- [x] 유효성 검증
    - [x] cartItemId List 유효성 검증
        - [x] 모든 cartItemId가 DB에 존재하는지 검증
        - [x] 사용자의 CartItem인지 검증
        - [x] 빈 List가 아닌지 검증
    - [x] couponId 검증
        - [x] DB에 존재하는지 검증
        - [x] 사용자의 쿠폰인지 검증
        - [x] 적용 가능한 쿠폰인지 검증
            - [x] 이미 사용한 쿠폰이 아닌지
            - [x] 만료기간이 남아있는지
            - [x] 최소적용금액과 주문한 상품 총 가격 비교
- [x] 요청 당시 price / quantity와 DB의 price / quantity 비교

## 회원의 특정 주문 조회

- [x] orderId로 Order를 조회한다.
- [x] 본인의 주문이 아닐시 예외
- [x] 주문 당시의 상품 정보로 조회된다.
- [x] 상품 총 가격, 할인 금액, 배송비를 반환한다.

## 회원의 주문 이력 조회

- [x] 회원의 주문 이력을 조회한다.
- [x] 주문 당시의 상품 정보로 조회된다.
