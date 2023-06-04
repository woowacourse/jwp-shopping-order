# jwp-shopping-order

## 할 일
- 로그 저장 파일 따로 지정하기
- 배포 스크립트 파일 어떻게 관리할지 생각해보기
  - 보안에 민감한 정보가 존재하지 않기 때문에 git에서 관리
- API 문서화
  - RestDocs 적용

## 2단계 기능 목록

### 주문
- [x] 장바구니 내 상품을 선택해서 주문할 수 있다.
- [x] 장바구니의 상품 중 주문 완료된 상품은 장바구니에서 제거한다.
- [x] 주문 금액에 따라 할인이 적용된다.
  - 5만원 이상 주문 시 2000원 할인
  - 10만원 이상 주문 시 5000원 할인
  - 20만원 이상 주문 시 12000원 할인
- [x] 주문 내역을 조회한다.
  - [x] 상세 정보 조회
    - 주문 번호
    - 각 품목 정보 - 품목명, 수량, 가격
    - 주문 날짜
    - 할인 전 금액
    - 전체 할인 금액
    - 할인 후 금액
  - [x] 전체 목록 조회
    - 주문 번호
    - 총 주문 금액
    - 품목명
    - 주문 날짜

### Order 도메인
- [x] 한 주문에는 여러 장바구니 상품이 포함될 수 있다.
  - [x] 어떠한 상품도 존재하지 않는다면 주문이 생성될 수 없다.
- [x] 총 주문 금액을 계산한다.
  - [x] 할인 적용
- [x] 주문 요청읋 한 Member와 장바구니 상품의 Member가 모두 동일한지 검사한다.

- 장바구니 상품을 통해 총 금액을 계산하는 역할의 객체가 있다면 어떨까?
  - Order 객체 내에서 계산까지 할 필요 없이, 계산이 완료된 금액을 전달받을 수 있다.
  - 하지만 클라이언트에 할인 전 금액/할인 금액/할인 후 금액을 전달해야 하는 상황이라면??
    - 그냥 3개의 정보를 상태로 갖는 객체를 만들고, 해당 객체가 주문 금액 계산을 하도록 하자. 충분히 의미가 있어 보인다.

- DiscountPolicy 인터페이스를 구현함으로써 다형성을 이용하고 싶었는데, 제대로 사용을 못하고 있는 것 같다.
  - 현재 OrderPrice 객체 내에서 FixedDiscountPolicy를 직접 생성해서 discount를 적용하고 있다.
  - 만약 외부에서 DiscountPolicy 객체를 넣어주려면, 주문의 금액을 계산하고 FixedDiscountPolicy.from(price)를 통해 반환받은 객체를 넣어줘야 한다.
  - 하지만 DiscountPolicy 객체를 생성하기 위해 주문의 금액을 계산하는 역할을 어느 계층이 가져야 할지 모르겠다.
  - -> 일급 컬렉션 객체인 CartItems를 생성해서 금액을 계산하고, 해당 금액을 토대로 DiscountPolicy를 뽑아내는 것으로 결정

- Order는 CartItems를 포함하고 있고, CartItems 내에 Member 정보가 있다. 그렇다면, Order는 Member 정보를 가지고 있지 않아도 괜찮을까?=
  - Order가 가지고 있는 List<CartItem>의 Member 정보가 모두 동일한지는 검사할 예정

**주문 생성 프로세스**
1. member 정보와, 주문에 포함되는 cartItem들의 id 정보가 담긴 요청을 받는다.
2. id에 해당하는 List<CartItem>을 조회한다.
   - 모든 상품들이 member의 장바구니에 담긴 상품인지 검증
3. 상품 금액에 맞는 할인 정책을 찾아서 Order 객체를 생성한다.
4. Order 객체를 통해 주문을 생성한다.
5. 주문이 완료되면 주문에 포함되어 있던 CartItem을 제거하고, OrderItem과 Order를 저장한다.
   - CartItem -> OrderItem 변환 과정 필요
6. 저장한 Order의 id를 반환한다.

### 인프라
- [x] AWS EC2 - DB 서버 인스턴스 생성
- [ ] 프로덕트 DB - MySQL 적용

### API
- [x] 주문 생성
- [x] 주문 내역 전체 조회
- [x] 주문 내역 단건 조회
- [x] 장바구니 단건 조회
- [x] 페이지네이션
  - 출력할 데이터 개수, 페이지 수로 요청받은 뒤 그에 맞는 데이터를 응답한다.
  - ex) 개수: 20, 페이지: 3 -> id 41~60에 해당하는 데이터로 응답
  - [x] 장바구니
  - [x] 상품

## 궁금한 내용
- Member가 CartItem을 가지도록 설계된 이유가 무엇일까?
  - 한 Member는 여러개의 CartItem을 가질 수 있다. 반대로, 한 CartItem은 여러 Member를 가질 수 없다. 즉, 1:N 관게이다.
  - Member가 List<CartItem>을 가지고 있을 때의 장단점은 무엇이 있을까?
    - 장점
      - Member가 다른 객체를 거치지 않고도 본인의 CartItem을 관리(추가/제거 등)할 수 있다. == 캡슐화
      - 개인적인 의견으로는 이 구조가 더 자연스럽게 보인다. (장점이라기보단 개인적인 의견.. 그래서 의문이 생긴듯)
    - 단점
      - Member를 조회할 때 CartItem까지 모두 조회해야 한다. == 결합도 증가 -> 객체 생성과 소멸의 복잡성이 올라감
  - CartItem이 Member를 가지고 있을 때의 장단점은?
    - 장점
      - CartItem은 도메인 특성상 무조건 Member에게 포함된다. 그러므로 Member와 함께 조회되는 과정 자체는 자연스럽다고 생각한다.
      - CartItem에게 있어서 Member 객체와의 의존성이 좀 더 낮아지며, 유연성이 높아진다.(CartItem을 가지는 Member가 변경되면 그냥 객체를 갈아끼워주면 됨)
        - 이 장점은 현재 도메인에서 무의미해보인다. 장바구니 상품이 다른 유저에게 이동될 일은 없을 것 같다.
    - 단점
      - Member가 매우 많은 정보(나이, 성별, 생일, 프로필 사진 등등...)를 가지고 있다고 가정하자. 모든 CartItem이 자신의 Member를 가지고 있는 것은, 불필요하게 많은 정보를 가지고 있는 것이 아닐까? -> 정보의 중복, 불필요한 의존성
      - CartItem을 수정하거나 삭제할 때 Member 객체가 이를 알도록 해야 하는데 이때의 관리가 복잡해진다.
  
- 1:N 관계에서 N이 1을 알고 있을 때, 문제가 발생할 수 있는 경우는 다음 상황인 것 같다.
  - Member가 많은 정보를 가지고 있다면 CartItem이 Member를 가지고 있는 것은 불필요한 의존성이 생기는 것이다.
    - 현재는 Member가 많은 정보를 갖고 있지 않고, 인증을 위해 필요한 정보만 가지고 있다. 그리고 CartItem은 무조건 인증이 필요하기 때문에 지금은 문제가 없는 것 같다. 만약 Member가 많은 정보를 갖게 된다면 그땐 인증만을 위한 새로운 객체를 분리하게 될 것 같다.
  - CartItem의 수정/삭제 발생을 Member는 알 수 없다. 별도의 메서드를 통해 관리해야 한다.
    - 이 문제가 발생한다는 것은 A라는 CartItem 객체가 제거됐는데, Member 객체는 이를 인지하지 못하고 A를 관리하고 있다고 착각하는 상황인 것 같다. 이는CartItem이 삭제되고 Member에 반영되기까지의 사이에 발생할 수 있는 것 같다. 생각해보면 이건 1:N 관계에서만 발생할 수 있는 문제가 아닌 것 같은데 멀티스레드 환경에서 이런 걸 고려해야 되는 걸까??.. 근데 Transaction을 잘 활용하면 이 문제는 해결할 수 있을 것 같다. 아무튼 당장은 크게 문제 삼지 않아도 될 것 같다.

- 주문엔 많은 상품이 포함될 수 있고, 상품은 많은 주문에 포함될 수 있다. 즉 N:N 관계인데 이를 어떻게 풀어낼까?
  1. 주문(order) 테이블 - 장바구니 상품(cart_item) 테이블의 다대다 관계를 풀어주는 매핑 테이블을 만들어준다.
     - 단점
        - 상품(product)을 참조하고 있는 장바구니 상품을 한 번 더 참조하게 된다.
        - 이렇게 되면 cart_item 데이터는 계속 참조되고 있기 때문에 지울 수 없다.
          - 주문된 장바구니 상품인지를 나타내는 column이 추가되어야 한다. 
          - cart_item은 주문이 완료된 후에도 지울 수 없기 때문에 데이터가 계속 쌓여간다.
  2. 주문(order) 테이블 - 상품(product) 테이블의 다대다 관계를 풀어주는 매핑 테이블 테이블을 만들어준다.
     - 장점
        - 장바구니에 존재하던 cart_item은 주문이 완료되면 제거되며, 같은 정보의 데이터를 매핑 테이블로 복사
          - cart_item에 데이터가 계속 쌓이는 것을 방지할 수 있다.
        - 주문이 장바구니에 종속적이지 않게 된다. -> 장바구니에 담지 않고도 바로 주문할 수도 있다.
        - Product에 종속적이게 되지 않은가? -> 장바구니 상품에 종속적인 것보단 나을 것 같다.
     - 단점
       - cart_item과 완전 같은 정보를 가진 테이블이 생성된다.
         - cart_item에 column 하나만 추가하면 해결할 수도 있지만 table을 생성하는 것이 큰 의미를 가질까?

- 도메인 객체와 Entity 객체를 동일시하고 다뤘을 경우, DB에서 넘어온 값을 객체로 조립해주는 과정이 필요했다. DB는 데이터를 객체로 저장하지 않고 식별자로 참조하며 저장하기 때문이다. 이떄 여러 테이블이 엮여 있다면 객체를 조립하기 위해 join을 하고, 다른 Dao를 호출해줘야 된다.
  - 뼈대 코드의 CartItemDao 조회 코드를 보면 알 수 있다.
- 이를 해결하고자 Repository 계층을 두었고, Dao에서는 join하지 않고 DB의 값 그대로를 담은 엔티티 객체로 내보낸다. 그리고 객체로 조립하는 과정은 Repository 계층에서 하도록 설계하였다.
  - Repository 계층과 엔티티 객체의 필요성에 대해서 느끼게 된 것 같다.

- ORDERS 테이블을 생성할 때, CREATE_AT 필드에 default 값을 넣어줬다. 하지만, 막상 데이터를 넣고 보니 create_at 필드가 null로 돼있는 문제를 발견함
  - INSERT INTO ORDERS(col1, col2) VALUES(val1, val2)와 같이 컬럼을 제한하지 않고, INSERT INTO ORDERS VALUES(val1, val2)와 같이 넣게 되면, 남은 컬럼은 값이 null로 들어간다.
  - SimpleJdbcInsert의 경우, 설정을 따로 하지 않으면 후자처럼 쿼리가 날라가는 것 같다. 
    - id를 제외하고 4개의 컬럼이 있다. 그중에 create_at을 제외한 3개 컬럼의 값만 Map에 넣어서 insert 했을때 created_at이 null로 들어갔음을 확인
    - 즉, 제공한 파라미터로만 insert 문을 실행한다.
    - https://stackoverflow.com/questions/20497985/why-does-springs-jdbc-templates-doesnt-use-the-tables-default-value
  - 해결방법은 다음 두가지다.
    1. SimpleJdbcInsert를 사용할 때, 자동 생성되는 column을 제외한 column을 명시해준다. (usingColumns() 메서드를 통해 명시해줄 수 있다)
       - 근데 이렇게 귀찮게 설정해줘야 한다면 SimpleJdbcInsert를 왜 써야 되나 의문이 생기긴 한다..
    2. SimpleJdbcInsert를 사용하지 않는다. 대신 키를 반환하기 위해서 KeyHolder를 이용해야 될 것 같다.
      - KeyHolder를 사용할 때도 key에 id,created_at 두 개가 모두 들어가는 것을 확인함. 자동 생성되는 값을 KeyHolder가 받아주는 듯함..
      - KeyHolder를 사용할 때의 문제점은 getKeys().get("id") 와 같이 id만 뽑아서 사용하던가, JdbcTemplate.update() 매개변수로 new String[]{"id"}를 넣어주며 id만 받을 것이라고 명시해주며 해결할 수 있다.
      - https://shanepark.tistory.com/383

- 전체 조회 API가 있는 상태에서 클라이언트의 요청에 따라 페이징 API를 구현했다. 근데 두 API의 url을 같게 설계해서 Request Mapping 과정에서 에러가 발생했다.
  1. url을 다르게 설계한다
     - 기존의 전체 조회를 /products/all 로 변경한다? -> REST API에 맞지 않는 선택인 것 같다.
     - 페이징 API url을 /products/page 와 같이 설정 -> 이미 쿼리스트링으로 page=3과 같이 넘기고 있기 때문에 아닌 것 같다.
  2. 서버 내부에서 페이징 관련 쿼리스트링을 받았을 떄와 안 받았을 떄를 다르게 처리한다.
     - 하나의 컨트롤러 메서드에서 이를 처리하려면 페이징을 할 때/안 할 때 모두 같은 타입의 DTO로 응답해야 한다.
       - 페이징 API의 응답에서 페이지네이션 관련 정보를 포함하기 때문에 기존의 설계는 각각 다른 타입을 반환하고 있었다.
     - 그러면, 전체 조회일 때도 Pagination 관련 정보가 담긴 응답을 할텐데 이땐 무슨 값을 넣어주면 좋을까?
     - 다르게 처리한다면 RequestParam 또는 ModelAttribute가 null일 때를 기준으로 처리해야 될까?
       - ArgumentResolver를 통해 객체로 바인딩하고, 파라미터가 없이 들어온다면 기본값을 설정해줌으로써 전체조회되도록??
     - +) 주노가 SpringDataWebProperties.Pageable이라는 키워듣를 알려줬다. 현재 내 코드에서 적합하게 사용할 수 있는지는 좀 더 찾아봐야 될 것 같다. 
  - 클라이언트 팀원들과 논의할 내용
    1. 페이징 API를 사용하면, 기존의 전체 조회 API는 더이상 쓰이지 않는 것인지?
       - 그렇다면 그냥 기존의 전체 조회 API 없애버리면서 Request Mapping 충돌 없애자
    2. 전체 조회 API를 그대로 남길 것이라면, 전체 조회 API의 응답으로도 pagination 정보를 보내는 게 좋을지?(의미 없는 값 넣어서)
## 추후 리팩토링
- [x] Dao 중복 코드 제거
- [ ] Dao 레벨에선 엔티티만 다루도록 수정 + Repository 레벨에서 객체 조립 후 반환

## 할일
- [x] 주문 생성 기능 서비스 테스트 작성
- [x] 주문 내역 조회시, response의 키 값 cartItems -> orderItems로 바꾸는 거 어떤지 팀원과 논의한 후에 변경하기
- [x] Product 조회 시에 is_deleted = 0인 row만 조회하도록 수정
