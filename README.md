# jwp-shopping-order

## 구현할 API
- 유저가 보유한 적립금을 요청한다
- 장바구니 아이템 목록을 조회한다(기존 API 수정)
  - 적립되는 비율을 반환한다
  - 적립금 사용 가능 여부를 반환한다
- 장바구니에 담은 상품을 주문할 수 있다
- 사용자별로 주문 목록을 확인할 수 있다
- 특정 주문의 상세 정보를 확인할 수 있다

## 수정할 기능 목록
- DB 테이블 수정에 따른 변화에 대응한다
- Member
  - 보유한 적립금을 update할 수 있다.
  - 새로운 멤버 등록 시 적립금을 입력받는다.
- Product
  - 포인트 적립비율을 수정할 수 있다.
  - 포인트 사용여부를 수정할 수 있다.
  - 새로운 상품 등록 시 포인트 적립비율 및 포인트 사용여부를 입력받는다.

## 구현할 기능 목록
- 주문이 들어왔을 때
  - [ ] 값 검증
    - [x] 요청으로 받은 originalPrice와 주문한 물품들의 실제 값들의 합이 같은지 확인한다.
    - [x] 요청으로 받은 pointToAdd와 주문한 물품들이 제공하는 포인트들의 합이 같은지 확인한다.
    - [x] 요청으로 받은 usedPoint가 실제 member가 가진 포인트보다 작거나 같은지 확인한다.
    - [x] 요청으로 받은 usedPoint가 주문한 물품들의 최대 사용 포인트보다 작거나 같은지 확인한다.
    - [ ] 값 검증이 실패했을 때 Conflict(409)을 전달한다.
  - [ ] 데이터베이스에 주문정보 저장
    - [ ] order 테이블에 주문 정보를 저장한다.
    - [ ] order_info 테이블에 주문 상세 정보를 저장한다.

## 생각해보면 좋은 문제
- 서버 부하 관련 처리 : 너무 많은 요청량이 들어올 경우 어떻게 처리할 수 있을까?
(ex. 반복적인 물품 수량 변경 요청의 경우 프론트에서 수량을 렌더링한 후 주문할 때 최종 수량만 백엔드에 전달한다)
- 동기화 관련 : 프론트엔드와 백엔드의 동기화를 어느 시점에 수행할 것인가?

### 장바구니 협업 공유 노션
[장바구니 협업 노션](https://quilt-dinghy-08e.notion.site/step2-da784bf6f78b4ce8baa89d489ceb227e)