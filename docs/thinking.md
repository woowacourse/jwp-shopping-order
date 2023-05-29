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
