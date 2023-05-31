# 기능 목록

### 상품

- 새로운 상품을 등록할 수 있다.
    - 상품의 이름을 지정한다.
        - 상품 이름은 중복될 수 있다.
    - 상품의 가격을 지정한다.
    - 상품의 이미지 파일을 추가한다.
- 등록된 상품을 검색할 수 있다.
    - 모든 상품을 검색할 수 있다.
    - Id 를 통해 하나의 상품을 검색할 수 있다.
        - 해당 Id의 상품이 없다면 요청은 실패한다.
- 등록된 상품을 업데이트할 수 있다.
    - 상품의 이름, 가격, 이미지 URL 을 업데이트 할 수 있다.
        - 해당 Id의 상품이 없다면 요청은 실패한다.
- 등록되 상품을 삭제할 수 있다.
    - 해당 Id의 상품이 없다면 요청은 실패한다.

### 장바구니

- 장바구니에 새로운 상품을 담을 수 있다.
    - 해당 Id의 상품이 없다면 요청은 실패한다.
- 장바구니에 담긴 유저의 상품들을 볼 수 있다.
- 장바구니에 담긴 유저의 상품을 업데이트할 수 있다.
    - 만약 해당 id 의 장바구니 아이템이 없으면 예외가 발생한다.
    - 만약 자신의 카트아이템이 아니라면 예외가 발생한다.
    - 요청받은 수량으로 상품을 변경한다.
        - 만약 수량을 0 으로 요청한다면 해당 상품은 장바구니에서 제거한다.
- 장바구니에 담긴 상품들을 삭제할 수 있다.
    - 만약 해당 id 의 장바구니 아이템이 없으면 예외가 발생한다.
    - 만약 자신의 카트아이템이 아니라면 예외가 발생한다.

### 재화 정책 - 쿠폰

- [x] 고정 금액 쿠폰을 제공한다.
    - [x] 쿠폰의 아이디가 있다.
    - [x] 쿠폰의 이름이 있다.
    - [x] 쿠폰의 할인금액이 있다.
        - [x] 할인 금액은 양수이다.
- [x] 쿠폰의 할인금을 반환한다.
- [ ] 로그인한 사용자의 쿠폰 리스트를 반환한다.
- [ ] 쿠폰은 주문 요청과 함께 요청한다.

### 주문

- [x] 구입할 장바구니 속 상품의 목록을 함께 받는다.
- [x] 적용 하고자하는 쿠폰의 Id를 함께 받는다.
- [x] 사용자가 결제할 것으로 예상한 금액 받는다.
- [ ] 주문에 대한 계산을 진행한다.
    - [x] 장바구니에 존재하는 상품인지 검증한다.
        - [x] 존재하지 않는 상품이 있다면 주문은 실패한다.
    - [ ] 상품들의 모든 금액과 배달료를 더하여 상품 금액을 계산한다.
        - [x] 주문 금액에 배달료를 더한다.
        - [ ] 배달료는 3000원 고정 금액으로 계산한다.
    - [ ] 쿠폰을 적용시킨다.
        - [ ] 쿠폰이 없을 경우에는 상품 금액 그대로 계산 금액을 결정한다.
        - [x] 쿠폰이 있을 경우에는 상품 금액에서 쿠폰 할인금을 차감하여 게산 금액을 결정한다.
        - [ ] 만약 할인금이 상품 금액보다 더 클 경우 주문을 취소한다.
- [ ] 만약 서버측 계산 금액과 예상 금액이 다를 경우 주문을 취소한다.
- [ ] 주문을 완료 단계를 진행한다.
    - [ ] 구매한 상품들은 장바구니에서 삭제한다.
    - [ ] 주문 내역을 저장한다.
        - [ ] 주문한 상품들의 정보들을 저장한다.
            - [ ] 상품의 식별할 수 있는 정보와 수량을 저장한다.

### 주문 목록

- [ ] 사용자의 주문 목록을 보여준다.
    - [ ] 주문 Id 를 보내준다.
    - [ ] 주문 상품들을 보여준다.
        - [ ] 상품의 ID 를 포함한다.
        - [ ] 상품의 수량을 포함한다.
        - [ ] 상품의 정보를 포함한다.
            - [ ] 상품의 아이디를 포함한다.
            - [ ] 상품의 이름을 포함한다.
            - [ ] 상품의 가격을 포함한다.
            - [ ] 상품의 이미지를 포함한다.

### 주문 상세

- [ ] 하나의 주문에 대한 상세 정보를 보여준다.
    - [ ] 주문 상품의 정보를 보여준다.
        - [ ] 주문 Id 를 보내준다.
        - [ ] 주문 상품들을 보여준다.
            - [ ] 상품의 ID 를 포함한다.
            - [ ] 상품의 수량을 포함한다.
            - [ ] 상품의 정보를 포함한다.
                - [ ] 상품의 아이디를 포함한다.
                - [ ] 상품의 이름을 포함한다.
                - [ ] 상품의 가격을 포함한다.
                - [ ] 상품의 이미지를 포함한다.
    - [ ] 주문의 총 가격을 보여준다.
