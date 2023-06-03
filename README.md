# jwp-shopping-order


<a href = "https://www.notion.so/step2-da784bf6f78b4ce8baa89d489ceb227e"> API 명세 (노션) </a>

# 기능 목록
---

## 기능 목록(API)
- [x] 상품 관련 API
  - [x] 상품 정보는 다음 두 정보까지 포함하도록 개선한다.
    - 포인트 적립비율
    - 포인트 적립 가능여부

- [x] 유저 관련 API
  - [x] 보유한 적립금을 조회할 수 있다.

- [x] 주문 관련 API
  - [x] 장바구니에 담은 상품들을 주문할 수 있다.
  - [x] 사용자별로 주문 목록을 확인할 수 있다.
  - [x] 사용자별로 특정 주문의 상세 정보를 확인할 수 있다.

## 기능 목록(Domain)
- [x] 상품 정보를 수정한다.
  - [x] 상품 정보는 포인트 적립비율, 포인트 적립 가능여부를 포함한다.

- [x] 유저는 적립금을 가질 수 있다.
  - [x] 유저에 대한 적립금 조회 요청 시, 보유한 적립금을 반환한다. 

- [x] 장바구니에 담은 상품을 주문할 수 있다.
  - [x] 상품은 적립금에 대한 정보를 가진다.
    - [x] 적립금을 가질 수 있다.
      - [x] 최대 사용 가능한 적립금보다 사용하고자 하는 적립금이 크다면 예외를 발생시킨다.
      - [x] 최대 사용 가능한 적립금을 계산할 때, 소수 첫째 자리에서 반올림한다.
    - [x] 적립금을 가지지 않을 수도 있다.
  - [x] 상품 ID 목록을 통해 주문을 발행한다.
  - [x] 사용한 포인트가 있다면, 사용자 포인트에서 차감한다. 

- [x] 사용자 별로 주문 목록을 확인할 수 있다.

- [x] 사용자 별로 특정 주문의 상세 정보를 확인할 수 있다.


## STEP 1 고민
- [x] `Product`라는 도메인 객체에 Boolean 타입 프로퍼티가 들어가가 됨(`pointAvailable`)
  - [x] 그렇다면 이는 객체 분리의 시점이라는 것인데, 분리를 하면 영속화/복원은 어떻게 할까?
- [x] 테이블 조인을 한다면 Repository와 Dao가 둘 다 필요하지 않다. 
  - [x] 조인된 결과 자체가 도메인 엔티티이기 때문에.
  - [x] 그렇다면 DAO, Repository가 둘 다 존재해야 하는 시점은 언제인가?
- [x] 조인 쿼리를 작성하다보니, 메소드가 엄청나게 길어지고 도메인 객체를 복원하는 코드도 복잡함.
  - [x] 하지만 성능 측면에서는 조인이 나을 것 같은데, 어플리케이션 레벨에서 조인을 수행해야 할 때는 언제일까?
- [x] `OrderInfoPersistenceAdapter`의 경우 package-private으로 두었음.
  - [x] `OrderInfo`는 `Order`라는 개념 하위에서만 존재한다. 단독으로 다뤄지는 경우는 없다.
  - [x] 그래서 외부에서 접근하지 못하도록 package-private으로 두어 `OrderPersistenceAdapter`가 접근하게 했는데, 좋은 방법일까?
- [x] 응답 JSON에서 키 값은 getter명을 따라가는 것 같다.
  - [x] 이에 대해 추후에 조금 더 공부해보기.
- [x] service에서 표현 계층의 request를 받는 형태로 작업하고 있음.
  - [x] 하지만, 표현 계층의 DTO를 사용하는 경우 service 내부 private 메소드에는 침투시키면 안될 것 같다는 생각.
  - [x] 양방향 의존성을 어느정도 용납하고 DTO를 그대로 받아와 쓰는 것인데... private 메소드까지 DTO를 침투시키면 결합도가 너무 높다.
  - [x] 따라서 `서비스 메소드의 첫줄에서 DTO를 변환시킨다!`라는 이야기가 나오는 듯 함.
