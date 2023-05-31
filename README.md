# jwp-shopping-order

### step1 기능 목록
- [x] 장바구니에 담긴 아이템 목록을 확인할 수 있다. (장바구니 아이템 목록 조회)
- [x] 상품을 장바구니에 추가할 수 있다. (장바구니 아이템 추가)
- [x] 장바구니에 담긴 아이템의 수량을 변경할 수 있다. (장바구니 아이템 수량 변경)
- [x] 장바구니에 담긴 아이템을 삭제할 수 있다. (장바구니 아이템 삭제)

### step2 기능 목록
- [x] 장바구니에 담은 상품을 주문할 수 있다. (주문 추가, 포인트 적립, 사용 포인트 감소, 장바구니 제거, 물품 재고 감소)
  - [x] 사용자에게 주문 단계에 필요한 정보(이메일, 가용 포인트 등)을 보여준다.
  - [x] 재고가 부족하거나 예상 금액보다 사용할 포인트를 높게 입력하면 주문이 실패한다.
  - [x] 사용 포인트가 가용 포인트보다 높으면 주문이 실패한다.
  - [x] 예상 금액과 실제 금액이 다르면 주문이 실패한다.
  - [x] 장바구니에 없는 물품의 주문은 실패한다.
  - [x] 존재하지 않는 장바구니나 타인의 장바구니면 주문이 실패한다.
- [x] 상품 주문 시 현실 세계의 쇼핑 서비스가 제공하는 재화 관련 요소 포인트 추가.(상품 주문 시 적용)
- [x] 사용자 별로 주문 목록을 확인할 수 있다.(주문목록 조회)
- [x] 특정 주문의 상세 정보를 확인할 수 있다. (주문 상세정보 조회)

# step2 API 명세

### 유저정보 조회 (포인트 조회)

#### Request

```json
GET /users HTTP/1.1
basic auth
```

#### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
	"email": "a@a.com",
	"point": 1000 // 현재 보유중인 포인트
	"earnRate": 0.01 // 적립률 1%
}
```

### 주문 요청

#### Request

```json
POST /orders HTTP/1.1
basic auth
```

```json
{
	"cartIds": [1, 2], // 주문할 카트 아이디
	"point": 1500, // 사용할 포인트
	"totalPrice": 32000 // 예상 총액
}
```

#### Response (성공)

```json
HTTP/1.1 201 OK
Content-Type: application/json
Location: "/orders/{id}"
```

#### Response (실패: 재고 부족)

```json
HTTP/1.1 409 Conflict
Content-Type: application/json

{
	"errorCode": 1
	"message": "1번 상품의 재고가 부족합니다."
}
```

#### Response (실패: 포인트 부족)

```json
HTTP/1.1 409 Conflict
Content-Type: application/json

{
	"errorCode": 2
	"message": "포인트가 부족합니다."
}
```

### 주문목록 조회

#### Request

```json
GET /orders HTTP/1.1
basic auth
```

#### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

[
{
  "orderId": 1,
  "createdAt": 2023-05-26,
  "orderItems": [
      {
        "productId": 10,
        "productName": "새우깡",
        "quantity": 3,
        "price": 1500,
        "imageUrl": "http://example.com/dfdf",
      },
      {
        "productId": 22,
        "productName": "감자깡",
        "quantity": 1,
        "price": 1200,
        "imageUrl": "http://example.com/abcd",
      }
    ],
  "totalPrice": 15000,
  "usedPoint": 1700,
  "earnedPoint": 300
}, 
{
  "orderId": 3,
  "createdAt": 2023-05-25,
  "orderItems": [
      {
        "productId": 10,
        "productName": "새우깡",
        "quantity": 3,
        "price": 1500,
        "imageUrl": "http://example.com/dfdf",
      },
      {
        "productId": 22,
        "productName": "감자깡",
        "quantity": 1,
        "price": 1200,
        "imageUrl": "http://example.com/abcd",
      }
    ],
  "totalPrice": 15000,
  "usedPoint": 1700,
  "earnedPoint": 200
}
]
```

### 주문 조회

#### Request

```json
GET /orders/{id} HTTP/1.1
basic auth
```

#### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "orderId": 1,
  "createdAt": 2023-05-26,
  "orderItems": [
    {
    "productId": 10,
    "productName": "새우깡",
    "quantity": 3,
    "price": 1500,
    "imageUrl": "http://example.com/dfdf",
    },
    {
    "productId": 22,
    "productName": "감자깡",
    "quantity": 1,
    "price": 1200,
    "imageUrl": "http://example.com/abcd",
    }
  ],
  "totalPrice": 15000,
  "usedPoint": 1700,
  "earnedPoint": 300
}
```
