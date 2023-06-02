# 생각의 흐름.

## MemberArgumentResolver에서 DB 회원 정보 조회

- MemberArgumentResolver는 ui 패키지에 위치한다.
- 여기서 MemberRepository를 활용하여 DB에 있는 회원 정보와 비교해도 되나? (email, password로 조회)
- 예전 프로젝트를 할 때는, jwtToken방식을 활용했기에 이와같은 고민을 하지 않았다. (token payload에 memberId를 담아주고, 이를 service단에서 member로 조회)
- 하지만 지금은 BasicLogin 방식을 사용한다.. email과 password를 담은 DTO같은 객체를 만들어서, 이도 service에서 하는것이 좋을까?
- 하지만 인증을 요구하는 모든 Service의 메서드에서 findMember 절차가 들어가게된다. --> 코드 중복..
- 이를 해결하기 위한게 ArgumentResolver인데.. 그러면 AuthService를 만들어서 ArgumentResolver에서 이를 호출하게 하는게 계층적으로 맞지 않을까?

### 결론

- AuthService를 만들고, 이를 ArgumentResolver에서 사용하자.

## Join을 할 것인가?

- DB에 접근하는 것은 비용이 크다 -> 최대한 DB에 접근하는 횟수를 날려야한다.
- 여러 테이블에 find 쿼리를 여러번 날리고 이를 도메인 구조로 Repository에서 바꿔주는 방법도 있지만, 이는 쿼리 개수가 늘어나기 때문에 성능상으로 좋지 않다.
- 그러면, Join문은 어디에 작성할것인가?
- Join된 결과물은 Domain과 맵핑된다. 따라서 Repository에서 하는 것이 좋아 보인다.
- 그렇다면 다른 쿼리문들은 DAO에서 하는 이유가 무엇인가..? 이도 Repository에서 하면 되는거 아닌가?
- 모든 DAO를 Repository에서 수행한다면, rowMapper는 Domain을 반환하나 ???
- DTO를 추가적으로 만드는 방법은 어떨까?

### 결론

- DB에 쿼리 날리는 횟수는 최소화하는게 좋다. -> Join을 사용한다.
- entity 여러개를 필드로 갖는 DTO를 만들어서 Dao->Repository 넘겨주고, Repository에서 이를 도메인으로 변환하자.

## Coupon 도메인 설계 구조.. enum?

- AmountCoupon은 discountAmount를 가지고, PercentCoupon은 discountPercent 가진다.
- DTO로 보내줄 땐, type이 AmountCoupond이면 discountPercent가 null이고 그런 구조..
- Coupon을 추상클래스로 두고 이를 상속하며 각 쿠폰에게 필요한 필드를 추가하는 클래스로 설계를 했다.
- 이 때, DTO에서 어떻게 넘겨주냐..?! DTO가 받는 타입은 추상클래스인 Coupon이다..

## DTO inner class?

- Request에서 DTO inner class를 활용하는것이 좋을까?
