# jwp-shopping-order

## API 연동 요구사항

- [x] 클라이언트 - 서버 연동
- [x] 상품, 장바구니 기능이 구현된 코드를 배포한다.
- [x] 상시 배포를 위한 배포 스크립트를 작성한다.
- [x] 클라이언트와 API 연동하며 발생하는 보안 이슈에 대응한다.
    - [x] 프론트엔드/백엔드 애플리케이션의 Origin이 달라서 요청을 처리하지 못하는 경우를 해결한다.

## 기능 요구사항

### 상품

- [x] 관리자 페이지에서 상품을 추가, 수정, 삭제할 수 있다. (기본 뼈대 코드)
- [x] 상품 목록을 보여준다.

### 장바구니

- [x] 장바구니에 담긴 아이템 목록을 확인할 수 있다. (장바구니 아이템 목록 조회)
- [x] 상품을 장바구니에 추가할 수 있다. (장바구니 아이템 추가)
  - [x] 이미 추가된 상품을 다시 추가할 경우 상품의 수량을 증가시킨다.
- [x] 장바구니에 담긴 아이템의 수량을 변경할 수 있다. (장바구니 아이템 수량 변경)
- [x] 장바구니에 담긴 아이템을 삭제할 수 있다. (장바구니 아이템 삭제)
    - [x] 장바구니에 담긴 아이템을 한꺼번에 삭제하게 API 명세를 수정한다.

### 주문

- [x] 장바구니에 담은 상품을 주문할 수 있다.
- [x] 사용자 별로 주문 목록을 확인할 수 있다.
- [x] 특정 주문의 상세 정보를 확인할 수 있다.
- [x] 주문 상태가 `결제 완료`인 경우, 주문을 취소할 수 있다.

### 쿠폰

- [x] 상품 주문 시 현실 세계의 쇼핑 서비스가 제공하는 재화 관련 요소를 최소 1가지 이상 추가한다.
    - [x] 장바구니에 담은 상품 주문 시, 사용자가 가지고 있는 쿠폰을 적용할 수 있다.
- [x] 쿠폰은 정액 할인을 기본으로 한다.
    - [x] 서버사이드에선 정액 할인과 정률 할인 모두 구현하되, 정액 할인 관련 API만 제공한다.
- [x] 사용자는 주문창에서 자신이 가진 쿠폰을 확인하고, 선택할 수 있다.

### 쿠폰 (admin)

- [x] 현재까지 발행된 모든 쿠폰을 최신순으로 확인할 수 있다.
- [x] 특정 쿠폰을 클릭 시, 해당 쿠폰의 상세 정보와 발행 현황을 확인할 수 있다.
- [x] 쿠폰 이름과 타입, 할인(금액 혹은 비율), 발행매수를 입력하면 쿠폰이 생성된다.
- [x] 쿠폰의 이름과 타입, 할인(금액 혹은 비율)을 수정할 수 있다.
- [x] 추가 발행 매수를 입력하면 이미 발행된 쿠폰을 추가적으로 발행할 수 있다.
- [ ] 해당 동작들을 admin 페이지에서 수행할 수 있다.
