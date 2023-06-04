# jwp-shopping-order

## 주문

### ✔️ 구매

### Request

```text
POST /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
	"cartItemIds":[1,2,3,4],
	"usePoint":1000
}
```

### Response

```text
HTTP/1.1 201 Created
Content-Type: application/json
Location: orders/{id}
```

### ✔️ 전체 주문 목록 조회

### Request

```text
GET /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```text
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
      "id": 2,               // 하나의 주문에 대한 id
      "price": 50000,       // 구매 전체 금액
      "orderDate": 2023-05-26T12:34
      "orders" : [           // 구매 전체 상품 리스트
          { "id": 1,           // 그 상품에 대한 id
            "quantity": 5,     // 그 상품의 개수
            "price": 20000,   // 그 상품의 가격
            "name": "피자",    // 그 상품의 이름
            "imageUrl": "http://example.com/pizza.jpg" // 그 상품의 이미지
          },
          { "id": 2,
            "quantity": 5,
            "price": 20000,
            "name": "피자2",
            "imageUrl": "http://example.com/pizza2.jpg"
          }
        ]
    },
]
```

### ✔️ 단일 주문 조회

### Request

```text
GET /orders/{orderId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```text
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,    
    "price": 50000,  
    "orderDate": "2023-05-26T12:34", 
    "orders" : [   
      { "id": 1, 
        "quantity": 5,  
        "price": 20000,  
        "name": "피자", 
        "imageUrl": "http://example.com/pizza.jpg"
      },
      { "id": 2,
        "quantity": 5,
        "price": 20000,
        "name": "피자2",
        "imageUrl": "http://example.com/pizza2.jpg"
      }
    ]
}
```

## 포인트

### ✔️ 회원이 가지고 있는 전체 포인트 조회

### Request

```text
GET /points HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```text
HTTP/1.1 200 Ok
Content-Type: application/json

{
	"points":10
}
```

### ✔️ 주문으로 적립된 포인트 조회

### Request

```text
GET orders/{id}/points HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```

### Response

```text
HTTP/1.1 200 Ok
Content-Type: application/json

{
	"points_saved":10
}
```

## 기능 요구사항
- [x] 주문
  - [x] 사용자는 자신의 장바구니에 담긴 상품을 주문할 수 있다.
    - [x] 주문이 된 상품은 장바구니에서 삭제된다.
  - [x] 사용자는 자신의 전체 주문 목록을 조회할 수 있다.
  - [x] 특정 주문의 상세 주문 내역을 조회할 수 있다.

- [x] 포인트
  - [x] 사용자는 상품을 주문할 때 포인트를 사용할 수 있다.
    - 자신이 보유한 포인트 중 얼마를 사용할지 결정할 수 있다.
    - 사용한 포인트는 사용자의 보유 포인트에서 차감된다.
  - [x] 구매한 상품의 구매 금액의 2.5%가 포인트로 적립된다.
    - 포인트는 **총 지불 금액에서만** 계산한다.
    - EX) 총 상품 금액이 10_000원이고, 포인트 사용 금액이 2_000원, 실제 지불 금액이 8_000원 이라면 포인트는 8_000원에 대해서만 적립된다.