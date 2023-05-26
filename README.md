# jwp-shopping-order

## 기능 구현 목록

### 기획

- 재화
    - 사용자는 장바구니 페이지에서 쿠폰을 사용할 수 있다.
        - 사용자가 쿠폰을 여러 개 사용할 수 있다.

    - 판매 상품에 세일을 적용할 수 있다.

- 주문
    - 사용자는 주문을 완료하면 주문 목록을 확인할 수 있다.
        - 주문 목록에서 하나를 선택하면 구입 가격 및 사용한 쿠폰들을 보여준다.

### 재화 플로우

- 재화
    - 회의
        - 마켓컬리, yes24 등 여러가지 서비스의 결제 과정을 프론트엔드 팀과 확인을 했습니다.
          <br> 프론트엔드 팀은 상태 관리를 하면서 사용자가 쿠폰을 사용할 때마다 가격 갱신이 되는 것을 보여주고 싶어했습니다.
          <br> 백엔드 팀에서도 적절하다고 생각했기에, 다음과 같은 전략을 구상했습니다.
    - 전략
        - 사용자가 쿠폰을 선택하면 프론트엔드 서버에서 쿠폰 종류와, id 값들을 보내준다.
        - 서버에서는 계산을 하고 `originalPrice & discountPrice`를 보내준다. (`discountPrice`는 할인 받는 금액을 의미합니다. 즉 1000원 할인 쿠폰이라면 1000원을
          의미합니다.)
        - 프론트엔드 서버에서 상태 관리를 하면서 사용자가 실시간으로 얼마를 지불해야하는지 보여준다.

- 세일
    - 상품 세일은 관리자 페이지에서 해줄 수 있다.
    - 프론트엔드 서버에서 메인 페이지에 할인 상품에 표시를 해주기 위해서 서버측에서는 `isOnSale`같은 boolean 타입과 가격을 보내준다. 혹은 `salePrice` 필드를 0원 혹은 x원으로
      보내주어, 0원이면 프론트 서버에서 처리한다.

### 도메인

- Member(id, email, password, Coupons)
    - email & password는 원시값포장 및 유효성 검사를 진행한다.

- Cart(id, Member, CartItems)

- CartItems(List<CartItem>)

- CartItem(id, Product, quantity)
    - 수량이 양수인지 확인한다.

- Product(id, name, price, imageUrl, isOnSale, Policy)
    - 상품 이름이 공백 혹은 빈 값인지 확인한다.
    - 가격이 양수인지 확인한다.

- Coupons(id, List<Coupon>)

- Coupon(id, name, Policy)
    - 쿠폰 이름이 공백 혹은 빈 값이 아닌지 확인한다.

- Policy(id, PricePolicy_ENUM)

- PricePolicy_ENUM(percentage)

- History(id, LocalDateTime, Member, Cart)
