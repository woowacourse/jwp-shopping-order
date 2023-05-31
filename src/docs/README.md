## 기능 목록

## API 명세

### OrderApi

| 구현  | Method |         URL         |   Http 상태코드   |                 Header                  | Request                                                               | Response|      Location       |       설명       |
|:---:|:------:|:-------------------:|:-------------:|:---------------------------------------:|:----------------------------------------------------------------------|:-------|:-------------------:|:--------------:|
|     |  GET   |      `/orders`      |   200 (OK)    | Authorization: basic<br/>email:password | -                                                                     | -      |          -          | 사용자의 주문을 조회한다. |
| [x] |  POST  |      `/orders`      | 201 (CREATED) | Authorization: basic<br/>email:password | {<br/><tab/><tab/>"cartIds": [1, 2, 3],<br/><tab/>"point": 1000<br/>} |-| `/orders/{orderId}` |   사용자가 주문한다.   |
|     |  GET   | `/orders/{orderId}` |   200 (OK)    | Authorization: basic<br/>email:password | -                                                                     |-|          -          |   해당 주문의 상세 정보를 조회한다.  |


### POST `/orders`

- 사용자가 주문한다

  - [x] 정상적으로 요청한 경우
    - `201 OK Response`를 반환한다.

    - 예외
      - 다음의 경우 `400 BAD Request`를 반환한다.
        - [x] 잘못된 예외 정보가 입력된 경우 예외를 던진다.
        - [x] 장바구니에 담기지 않은 상품을 입력하는 경우 예외를 던진다.
        - 포인트에 잘못된 값을 입력하는 경우 예외를 던진다.
          - [x] 소유하고 있는 포인트보다 더 많은 포인트를 사용한 경우 예외를 던진다.
          - [x] 포인트에 음수를 입력하는 경우 예외를 던진다.

### GET `/orders`

- 사용자의 주문을 조회한다.

### GET `/orders/{orderId}`

- 해당 주문의 상세 정보를 조회한다.

  - [ ] 정상적으로 요청한 경우
    - `200 OK Response`를 반환한다.

  - 예외
    - 다음의 경우 `400 BAD Request`를 반환한다.
      - [ ] 잘못된 예외 정보가 입력된 경우 예외를 던진다.
      - [ ] 아이디가 잘못 입력된 경우 예외를 던진다.


