# jwp-shopping-order

## API 명세

[API 명세](API.md)

## 요구사항 정의

### 주문

- [x] 상품 주문 기능
    - [x] 장바구니에 담은 상품을 주문할 수 있다.
- [x] 주문 조회 기능
    - [x] 사용자 별 주문 목록을 확인할 수 있다.
    - [x] 특정 주문의 상세 정보를 확인할 수 있다.

### 포인트

- [x] 포인트 적립 기능
    - [x] 주문 금액의 1%가 포인트로 적립된다.

- [x] 포인트 사용 기능
    - [x] 보유한 포인트를 사용할 수 있다.

- [x] 사용자가 가진 포인트 조회 API
- [x] 적립되는 포인트 조회 API

## 리팩토링 목록

- [x] 네이밍 변경
    - [x] 메소드 네이밍 역할에 맞게 수정
    - [x] 스네이크케이스 -> 카멜케이스로 수정
- [x] 예외 처리
    - [x] 커스텀 예외 추가
    - [x] 기존 적립되어 있는 포인트보다 많은 포인트 사용시 예외 처리
- [x] Transaction 적용
- [ ] 테스트 추가
