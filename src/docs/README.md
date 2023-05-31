## 기능 목록

## API 명세

### OrderApi

| 구현 | Method |         URL         |   Http 상태코드   |                 Header                  | Request                                                               | Response|      Location       |       설명       |
|:--:|:------:|:-------------------:|:-------------:|:---------------------------------------:|:----------------------------------------------------------------------|:-------|:-------------------:|:--------------:|
|    |  GET   |      `/orders`      |   200 (OK)    | Authorization: basic<br/>email:password | -                                                                     | -      |          -          | 사용자의 주문을 조회한다. |
|    |  POST  |      `/orders`      | 201 (CREATED) | Authorization: basic<br/>email:password | {<br/><tab/><tab/>"cartIds": [1, 2, 3],<br/><tab/>"point": 1000<br/>} |-| `/orders/{orderId}` |   사용자가 주문한다.   |
|    |  GET   | `/orders/{orderId}` |   200 (OK)    | Authorization: basic<br/>email:password | -                                                                     |-|          -          |   해당 주문의 상세 정보를 조회한다.  |
