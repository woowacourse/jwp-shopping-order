# jwp-shopping-order
## 1. 기능요구사항
### 회원관리
- 등록되어 있는 회원만 사용 가능 
  - [x] email: `a@a.com`, password: `1234`
  - [x] email: `b@b.com'`, password: `1234`
### 상품관리
- [x] 상품을 추가할 수 있다.
- [x] 상품을 조회할 수 있다.
- [x] 상품을 삭제할 수 있다.
- [x] 상품을 변경할 수 있다.
### 장바구니관리
- [x] 회원정보로 장바구니를 조회할 수 있다.
- [x] 장바구니에 상품을 추가할 수 있다.
- [x] 장바구니에 담긴 상품의 수량을 변경할 수 있다.
- [x] 장바구니에 상품을 삭제할 수 있다.
### 주문관리
- [ ] 장바구니에 담긴 상품을 주문할 수 있다.
  - [ ] 장바구니에 담긴 특정 상품만 선택하여 주문할 수 있다.
  - [ ] 항상 장바구니에 담긴 특정 상품의 수량만 주문할 수 있다.
  - [ ] 주문시 포인트를 사용할 수 있다.
  - [ ] 주문시 포인트가 적립된다.
- [ ] 회원정보로 주문목록을 조회할 수 있다.
- [ ] 특정주문의 상세정보를 조회할 수 있다.
### 포인트관리
- [ ] 회원정보로 포인트를 조회할 수 있다.
  - [ ] 만료기한이 지나지 않은 포인트만 조회할 수 있다.
- [ ] 포인트는 주문시에만 적립된다.
- [ ] 포인트는 유효기한이 있다.
- [ ] 포인트소모는 만료기한이 가까운 포인트부터 적용된다.
- [ ] 만료기한이 지난 포인트는 쓸 수 없다.
  - [ ] 데이터베이스에서는 지워지지 않는다.

## API 명세
### 1. 상품
<br>

#### [ 상품 목록 조회 ]
##### Request
``` json
GET /products HTTP/1.1
```
##### Response
``` json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "name": "치킨",
        "price": 10000,
        "imageUrl": "http://example.com/chicken.jpg"
    },
    {
        "id": 2,
        "name": "피자",
        "price": 20000,
        "imageUrl": "http://example.com/pizza.jpg"
    }
]
```
<br>

#### [ 상품 조회 ]
##### Request
``` json
GET /products/{productId} HTTP/1.1
```
##### Response
``` json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "치킨",
    "price": 10000,
    "imageUrl": "http://example.com/chicken.jpg"
}
```
<br>

#### [ 상품 추가 ]
##### Request
``` json
POST /products HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```
##### Response
``` json
HTTP/1.1 201 Created
Location: /products/{productId}
```
<br>

#### [ 상품 수정 ]
##### Request
``` json
PUT /products/{productId} HTTP/1.1
Content-Type: application/json

{
    "name": "부리또",
    "price": 30000,
    "imageUrl": "http://example.com/burrito.jpg"
}
```
##### Response
``` json
HTTP/1.1 200 OK
```
<br>

#### [ 상품 삭제 ]
##### Request
``` json
DELETE /products/{productId} HTTP/1.1
```
##### Response
``` json
HTTP/1.1 204 No Content
```
<br>

### 2. 장바구니
<br>

#### [ 장바구니 아이템 목록 조회 ]
##### Request
``` json
GET /cart-items HTTP/1.1
Authorization: Basic ${credentials}
```
##### Response
``` json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "quantity": 5,
        "product": {
            "id": 1,
            "price": 10000,
            "name": "치킨",
            "imageUrl": "http://example.com/chicken.jpg"
        }
    },
    {
        "id": 2,
        "quantity": 1,
        "product": {
            "id": 2,
            "price": 20000,
            "name": "피자",
            "imageUrl": "http://example.com/pizza.jpg"
        }
    }
]
```
<br>

#### [ 장바구니 아이템 추가 ]
##### Request
``` json
POST /cart-items HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "productId": 1
}
```
##### Response
``` json
HTTP/1.1 201 Created
Location: /cart-items/{cartItemId}
```
<br>

#### [ 장바구니 아이템 수량 변경 ] 
##### Request
``` json
PATCH /cart-items/{cartItemId} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
    "quantity": 3
}
```
##### Response
``` json
HTTP/1.1 200 OK
```
<br>

#### [ 장바구니 아이템 삭제 ] 
##### Request
``` json
DELETE /cart-items/{cartItemId}
Authorization: Basic ${credentials}
```
##### Response
``` json
HTTP/1.1 204 No Content
```
<br>

### 3. 주문
<br>

#### [ 주문 요청 ] 
##### Request
``` json
POST /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

{
	"cartIds": [1, 2, 3], // 주문할 카트 아이디
	"point": 1500, // 사용할 포인트
	"totalPrice": 32000 // 총액
}
```
##### Response
``` json
HTTP/1.1 201 Created
Content-Type: application/json
Location: "/orders/{id}"
```
<br>

#### [ 주문 목록 조회 ]
##### Request
``` json
GET /orders HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json

```
##### Response
``` json
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
<br>

#### [ 주문 조회 ]
##### Request
``` json
GET /orders/{id} HTTP/1.1
Authorization: Basic ${credentials}
Content-Type: application/json
```
##### Response
``` json
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
```
