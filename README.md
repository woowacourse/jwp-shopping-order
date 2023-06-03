# jwp-shopping-order

## API 연동 요구사항

- [x] 클라이언트 - 서버 연동
- [x] 상품, 장바구니 기능이 구현된 코드를 배포한다.
- [x] 상시 배포를 위한 배포 스크립트를 작성한다.
- [x] 클라이언트와 API 연동하며 발생하는 보안 이슈에 대응한다.
    - [x] 프론트엔드/백엔드 애플리케이션의 Origin이 달라서 요청을 처리하지 못하는 경우를 해결한다.

## 기능 요구사항

- [API 명세서](API.md)

### 상품

- [x] 관리자 페이지에서 상품을 추가, 수정, 삭제할 수 있다. (기본 뼈대 코드)
- [x] 상품 목록을 보여준다.

### 장바구니

- [x] 장바구니에 담긴 아이템 목록을 확인할 수 있다. (장바구니 아이템 목록 조회)
- [x] 상품을 장바구니에 추가할 수 있다. (장바구니 아이템 추가)
- [x] 장바구니에 담긴 아이템의 수량을 변경할 수 있다. (장바구니 아이템 수량 변경)
- [x] 장바구니에 담긴 아이템을 삭제할 수 있다. (장바구니 아이템 삭제)

### 주문

- [x] 장바구니에 담은 상품을 주문할 수 있다. (주문 추가)
- [x] 사용자 별로 주문 목록을 확인할 수 있다. (주문 목록 조회)
- [x] 특정 주문의 상세 정보를 확인할 수 있다. (주문 상세 조회)
- [x] 특정 주문을 삭제할 수 있다. (주문 삭제)
- [x] 특정 주문의 상태를 결제 취소로 변경할 수 있다. (주문 취소)

### 쿠폰

- [x] 장바구니에 담은 상품 주문 시, 사용자가 가지고 있는 쿠폰을 적용할 수 있다.
    - [x] 쿠폰은 정액 할인을 기본으로 한다.
    - [x] 주문 당 1개의 쿠폰만 적용할 수 있다.
    - [x] 주문에 사용한 쿠폰은 다시 사용할 수 없다.
    - [x] 쿠폰이 사용된 주문이 결제 취소되면, 다시 사용 가능한 상태가 된다.
- [x] 사용자가 가진, 사용 가능한 쿠폰 목록을 확인할 수 있다.

### 쿠폰 (admin)

- [ ] 현재까지 발행된 모든 쿠폰을 최신순으로 확인할 수 있다.
- [ ] 특정 쿠폰을 클릭 시, 해당 쿠폰의 상세 정보와 발행 현황을 확인할 수 있다.
- [ ] 쿠폰 이름과 타입, 할인(금액 혹은 비율), 발행매수를 입력하면 쿠폰이 생성된다.
- [ ] 쿠폰의 이름과 타입, 할인(금액 혹은 비율)을 수정할 수 있다.
- [ ] 추가 발행 매수를 입력하면 이미 발행된 쿠폰을 추가적으로 발행할 수 있다.
