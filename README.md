# 🧺 장바구니 협업 미션

## Pair: 져니 [⛄️](http://github.com/cl8d), 라온 [😊](https://github.com/mcodnjs), 쥬니 [😎](https://github.com/cpot5620)

## ✔ 기능 요구사항
### 사용자

- [x] 사용자를 추가할 수 있다.
  - [ ] 사용자는 처음 가입하면 신규 가입 쿠폰을 받는다.
- [x] 특정 사용자의 정보를 받을 수 있다.
- [x] 모든 사용자의 정보를 받을 수 있다.
- [ ] 사용자는 본인이 가지고 있는 쿠폰 목록을 조회할 수 있다.
- [x] 사용자는 로그인할 수 있다.

### 장바구니

- [x] 특정 사용자의 장바구니 리스트를 조회할 수 있다.
- [x] 특정 사용자의 장바구니에 상품을 추가할 수 있다.
- [x] 이미 추가된 장바구니 상품의 수량을 변경할 수 있다.
  - [x] 수량을 0으로 변경하면 장바구니에서 상품이 제거된다.
- [x] 장바구니의 상품을 제거할 수 있다.
- [x] 장바구니의 상품을 여러 개 제거할 수 있다.


### 상품
- [x] 상품 리스트를 반환한다.
- [x] 상품의 아이디로 상품을 조회한다.
- [x] 상품을 생성한다.
- [x] 상품 정보를 업데이트한다.
- [x] 상품을 삭제한다.

### 상품 주문
- [ ] 상품을 주문할 수 있다.
  - 상품의 아이디, 수량, 쿠폰 아이디를 받는다. 
  - 쿠폰은 최대 1매만 적용할 수 있다.
- [ ] 사용자의 전체 주문 내역을 조회할 수 있다.
- [ ] 사용자의 특정 주문 번호에 대한 내역을 조회할 수 있다.

### 쿠폰
- [x] 쿠폰을 추가할 수 있다.
- [x] 쿠폰 정보를 삭제할 수 있다.
- [x] 전체 쿠폰 정보를 조회할 수 있다.
- [x] 특정 쿠폰 정보를 조회할 수 있다.

### 추후 개발 사항
- [ ] 사용자 권한 분리 (어드민 / 일반 사용자)
- [ ] 어드민만 상품 추가, 쿠폰 추가 할 수 있도록 생성
- [ ] 에러 메시지에 아이디 정보도 담아서 주는 걸로...?

---

## DB 다이어그램
<img src="https://raw.githubusercontent.com/Cl8D/jwp-shopping-order/step1/src/main/resources/static/file/db-diagram.png">

---

## HTTP 요청 가이드
- http 디렉터리를 http client를 활용하여 실행하시면 더 편하게 API 요청을 진행하실 수 있습니다!
- Run With 옵션을 `local`로 설정해 주세요.

### 상품 API (ProductController)
- product.http

|              | HTTP method |     요청 URI     |
|:------------:|:-----------:|:--------------:|
|  **상품 추가**   |    POST     |   /products    |
|  **상품 수정**   |     PUT     |   /products    |
| **상품 단일 조회** |     GET     | /products/{id} |
| **상품 전체 조회** |     GET     |   /products    |
|  **상품 삭제**   |   DELETE    | /products/{id} |


### 사용자 API (MemberController)
- member.http

|               | HTTP method |    요청 URI    |
|:-------------:|:-----------:|:------------:|
|  **사용자 추가**   |    POST     | /users/join  |
|  **사용자 로그인**  |    POST     | /users/login |
| **사용자 단일 조회** |     GET     | /users/{id}  |
| **사용자 전체 조회** |     GET     |    /users    |

### 장바구니 API (CartItemController)
- cart-item.http

|                   | HTTP method |       요청 URI        |
|:-----------------:|:-----------:|:-------------------:|
|    **장바구니 추가**    |    POST     |     /cart-items     |
| **장바구니 상품 수량 수정** |    PATCH    |  /cart-items/{id}   |
|  **장바구니 상품 조회**   |     GET     |     /cart-items     |
|  **장바구니 상품 삭제**   |   DELETE    |  /cart-items/{id}   |
| **장바구니 상품 다중 삭제** |   DELETE    | /cart-items?ids=1,2 |

### 쿠폰 API (CouponController)
- coupon.http

|              | HTTP method |    요청 URI     |
|:------------:|:-----------:|:-------------:|
|  **쿠폰 추가**   |    POST     |   /coupons    |
| **쿠폰 단일 조회** |     GET     | /coupons/{id} |
| **쿠폰 전체 조회** |     GET     |   /coupons    |
|  **쿠폰 삭제**   |   DELETE    | /coupons/{id} |
