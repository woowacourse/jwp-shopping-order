# jwp-shopping-order

- [ ] 장바구니에 담은 상품을 주문할 수 있다.
    - [ ] 주문 한 내역을 응답으로 반환해야한다.
        - [x] OrderRequestDto (List<Long> id 임)
        - [x] Service 로 넘겨서 CartItem 의 id 를 검증하려 했으나, 하지 않는 것으로 판단
        - [ ] CartItem 을 조회하고, 그리고 CartItem 안에 있는 Product 들도 조회를 해야함
        - [ ] (Controller Resolving) Member, CartItem 을 담은 사용자 member_id (검증 필요)
        - [ ] Order 로 만들어주는 과정이 필요할 것 같음
        - [ ] Order <OrderProduct> 를 Repository 에다가 넘겨서 저장?
        - [ ] Order - (id, timestamp, Member, OrderProduct) (만들어주는 과정에서 검증도)
        - [ ] Order - id 가 채워져서 넘어올 듯, OrderProduct 도 id 가 다 채워져서
        - [ ] Order -> OrderResponseDto 를 만들어서 반환
        - [ ] 반환하기 이전에 List<Long> id 로 삭제
        - [ ] OrderController ->, /orders -> PostMapping
        - [ ] Service -> 주문 하는 기능
        - [ ] Repository -> Order, OrderProduct 를 같이 저장 (OrderRepository 하나에서)
        - [ ] CartItemDao... OrderRepository 삭제도 OrderRepository
        - [ ] Dao -> Order 저장, OrderProduct 저장 (Dao 가 2개)
    - [ ] 장바구니에서는 삭제 되어야 한다.
    - [ ] 주문 내역 테이블에 저장이 되어야한다.
- [ ] 상품 주문 시 현실 세계의 쇼핑 서비스가 제공하는 재화 관련 요소를 최소 1가지 이상 추가한다.
    - 재화 관련 요소: 쿠폰, 포인트, 할인 등
    - 예) 5만원 이상 주문 시 전체 금액에서 10% 할인이 된다.
- [ ] 사용자 별로 주문 목록을 확인할 수 있다.
- [ ] 특정 주문의 상세 정보를 확인할 수 있다.

### 주문하기 - 쿠폰적용 하는 경우

POST http://localhost:8080/order
Accept: application/json
Content-Type: application/json

```json
{
  "cartItemIds": [
    1,
    2
  ],
  "couponId": 1
}
```

### response

HTTP/1.1 200
Content-Type: application/json

```json
{
  "id": 1,
  "orderProducts": [
    {
      "productResponseDto": {
        "id": 1,
        "name": "치킨",
        "price": 10000,
        "imageUrl": "http://test.jpg"
      },
      "quantity": 1
    }
  ],
  "timestamp": "2023-05-30T17:00:00",
  "originPrice": 10000,
  "discountPrice": 5000,
  "totalPrice": 5000
}
```

### 주문하기 - 쿠폰적용 하지 않는 경우

POST http://localhost:8080/order
Accept: application/json
Content-Type: application/json

```json
{
  "cartItemIds": [
    1,
    2
  ]
}
```

### response

HTTP/1.1 200
Content-Type: application/json

```json
{
  "id": 1,
  "orderProducts": [
    {
      "productResponseDto": {
        "id": 1,
        "name": "치킨",
        "price": 10000,
        "imageUrl": "http://test.jpg"
      },
      "quantity": 1
    }
  ],
  "timestamp": "2023-05-30T17:00:00",
  "originPrice": 10000,
  "discountPrice": 0,
  "totalPrice": 10000
}
```

- 주문하기에서 해야할 것들 
- [x] Order 저장
- [x] OrderProduct 저장
- [x] Coupon 적용해서 Coupon 까지 저장
- [ ] cart-item 에서 삭제 
  - [ ] 결제가 되었을 때 삭제되어야함
  - [ ] 그렇기 때문에, Order 에 저장하기 이전에 삭제하면 안된다.
  - [ ] Cart-Item 을 삭제하는 행위는 들어온 Request 를 그대로 활용해서 삭제한다.
  - [ ] 그대로 OrderResponseDto 에 담아서 반환한다.
    - [ ] 이 괒어에서 필요한 것, OrderRepository 에 저장, OrderProduct 저장, CartItem 삭제

- 이제 남은 것
- 코드 리팩토링
- 기능 요구사항 정리 (현재 이 파일)
- 서브 모듈을 이용한 Mysql 관리
- 서버에 배포 