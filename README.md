# jwp-shopping-order

### 상품

| Method | URI                   | Description |
|--------|-----------------------|-------------|
| POST   | /products             | 상품 추가       |
| GET    | /products             | 상품 목록 조회    |
| GET    | /products/{id}        | 특정 상품 조회    |
| PUT    | /products/{productId} | 상품 수정       |
| DELETE | /products/{productId} | 상품 삭제       |

### 장바구니

| Method | URI                      | Description    |
|--------|--------------------------|----------------|
| POST   | /cart-items              | 장바구니 아이템 추가    |
| GET    | /cart-items              | 장바구니 아이템 목록 조회 |
| PATCH  | /cart-items/{cartItemId} | 장바구니 아이템 수량 변경 |
| DELETE | /cart-items/{cartItemId} | 장바구니 아이템 삭제    |

### 주문
[orders.http](./http/orders.http)

| Method | URI                  | Description                                           |
|--------|----------------------|-------------------------------------------------------|
| POST   | /orders              | 주문 추가                                                 |
| GET    | /orders?page={value} | 최신 순 ({value - 1} * 10 + 1 ~ {value} * 10)번째 주문 목록 조회 |
| GET    | /orders/{orderId}    | 주문 상세 조회                                              |
| DELETE | /orders/{orderId}    | 주문 취소                                                 |

- [x] 주문 시 사용할 포인트는 0 이상이어야 한다.
- [x] 주문에 포함된 상품 id는 1 이상이어야 한다.
- [x] 주문할 상품의 종류는 1가지 이상이어야 한다.
- [x] 주문할 상품의 수량은 1개 이상이어야 한다.


- [x] 다른 사용자의 주문을 조회하면 예외가 발생한다.


- [x] 주문 추가 시 보유한 포인트보다 많은 포인트를 사용하면 예외가 발생한다.
- [x] 주문 추가 시 존재하지 않는 상품의 id가 포함되면 예외가 발생한다.
- [x] 주문 추가 시 지불할 금액보다 많은 포인트를 사용하면 예외가 발생한다.


- [x] 결제 완료 상태인 주문은 취소가 가능하다
- [x] 취소하려는 주문 건으로 지급된 포인트가 이미 사용되었다면 주문 취소가 불가능하다
### 포인트
[points.http](./http/points.http)

| Method | URI     | Description           |
|--------|---------|-----------------------|
| GET    | /points | 현재 포인트 및 소멸 예정 포인트 조회 |

- 포인트는 지불 금액에 따라 일괄 적립된다.
  - 5만원 미만: 5%
  - 5만원 이상 10만원 미만: 8%
  - 10만원 이상: 10%
  - 10만원 이상: 10%

  - 포인트는 주문 즉시 지급된다
  - 포인트의 유효기간은 90일이다.
  - 포인트는 유효기간이 남지 않은 순서로 사용된다.
  - 주문 취소 시 반환되는 포인트는 지급 시점과 같은 유효기간을 가진다.
    - 반환될 포인트가 이미 유효기간이 지났다면 지급되지 않는다.
