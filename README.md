# jwp-shopping-order

## 구현할 기능 목록
- [x] 장바구니에 담은 상품을 주문할 수 있다.
  - [x] 총 금액에 할인을 적용한다.
    - 상품 할인이 있는 경우 상품 할인만 적용
    - 상품 할인이 없는 경우 멤버 등급 할인 적용
  - [x] 배송비 책정
    - 50,000원 미만 구매 시 기본 배송료 3000원
    - 50,000원 이상 구매 시 배송료 0원
  - [x] 주문 요청된 금액 검증
    - 요청된 할인되지 않은 상품 전체 가격을 비교한다.
    - 요쳥된 할인된 상품 전체 가격을 비교한다.
    - 요쳥된 상품 할인 금액을 비교한다.
    - 요청된 멤버 할인 금액을 비교한다.
    - 요청된 배송료를 비교한다.
  - [x] 주문 내역을 저장한다.
  - [x] 장바구니 내 구매한 상품을 삭제한다.
  - [x] 사용자 총 누적 금액을 확인해 등급 상승 여부 파악
- [x] 사용자 별로 주문 목록을 조회할 수 있다.
- [x] 특정 주문의 상세 정보를 조회할 수 있다.
- [x] 사용자 정보를 조회 할 수 있다.
- [ ] 장바구니 금액 정보를 조회할 수 있다.
- [x] 상품 조회 시 응답 값 수정
