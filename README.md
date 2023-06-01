# jwp-shopping-order

## step2

### Domain
- [x] Order
  - [x] 필드  
    - [x] id
    - [x] Member (회원 객체)
    - [x] OrderInfos(OrderInfo 일급 콜렉션 객체)
    - [x] originalPrice (할인 전 원가)
    - [x] usedPoint (사용할 적립금)
    - [x] pointToAdd (추가될 적립금)
  - [x] 모든 물품의 가격과 수량에 따른 originalPrice를 구한다.
    - [x] 각 물품의 가격과 수량에 따른 가격들을 더한다.
    - [x] 사용할 적립금만큼 가격을 뺀다.
  - [x] 예외 처리
    - [x] [제한 사항] 구한 originalPrice와 프론트에서 보낸 originalPrice가 같아야 한다.
    - [x] [제한 사항] 구한 pointToAdd와 프론트에서 보낸 pointToAdd가 같아야 한다.
    - [x] [제한 사항] 적립금 사용 시, '0 ~ pointAvailable이 true인 물품들의 가격 합' 의 범위에 속해야 한다.

- [x] Member
  - [x] 사용할 적립금만큼 뺀다.
  - [x] 적립될 적립금만큼 더한다.
  - [x] 예외 처리
    - [x] [제한 사항] 적립금 사용 시, 현재 보유한 적립금보다 크거나 0 미만일 수 없다.

- [x] OrderInfo
  - [x] 필드 
    - [x] id
    - [x] Product (상품 객체)
    - [x] name (샀을 당시의 상품 이름)
    - [x] price (샀을 당시의 가격)
    - [x] imageUrl (샀을 당시의 상품 이미지)
    - [x] quantity (샀을 당시의 수량)
  - [x] 물품 가격과 수량에 따른 가격을 구한다.

- [x] Product
  - [x] 포인트 적립금을 구한다.

### API
- [ ] 장바구니
  - [x] 장바구니 아이템 목록을 조회한다.(기존 API 수정)
  - [ ] 장바구니에 담은 상품을 주문할 수 있다.
    - [x] 정상 주문
    - [ ] 예외 처리
      - [x] [제한 사항] 구한 originalPrice와 프론트에서 보낸 originalPrice가 같아야 한다.
      - [x] [제한 사항] 구한 pointToAdd와 프론트에서 보낸 pointToAdd가 같아야 한다.
      - [x] [제한 사항] 적립금 사용 시, '0 ~ pointAvailable이 true인 물품들의 가격 합' 의 범위에 속해야 한다.
      - [x] [제한 사항] 적립금 사용 시, 현재 보유한 적립금보다 크거나 0 미만일 수 없다.
        - 상품마다 최대 적립을 계산하고, 최대 사용 가능한 적립금을 도출함
        - 최대 사용 가능한 적립금보다 사용하고자 하는 적립금이 크다면 예외 케이스
      - [ ] [제한 사항] 적립금 일정 이상 금액만 사용할 수 있다 (아직 정하지 않음)

- [x] 포인트(적립금) 조회
  - [x] 유저가 보유한 적립금을 요청한다.

- [x] 사용자 별로 주문 목록을 확인할 수 있다.

- [x] 특정 주문의 상세 정보를 확인할 수 있다.
  - [x] 정상 조회
  - [x] 예외 처리
    - [x] [제한 사항] 해당 멤버가 소유하지 않은 주문 번호를 특정할 시 예외 처리

- [ ] 예외 처리
  - [ ] 사용자 인증 정보가 잘못되었을 경우
    - HTTP/1.1 401 Unauthorized
  - [ ] 프론트엔드에서 계산한 금액과 실제 금액이 다른 경우
    - HTTP/1.1 409 Conflict
