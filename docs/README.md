# 구현 기능 목록

## 상품
- [x] 상품을 추가할 수 있다.
- [x] 상품을 조회할 수 있다.
- [x] 상품을 수정할 수 있다.
- [x] 상품을 삭제할 수 있다.

## 장바구니
- [x] 장바구니에 상품을 추가할 수 있다.
- [x] 장바구니에 상품을 조회할 수 있다.
- [x] 장바구니에 상품의 수량을 수정할 수 있다.
- [x] 장바구니에 상품을 삭제할 수 있다.

## 포인트
- [x] 첫 가입 시 기본 포인트는 5,000원을 갖는다.
- [x] 사용자가 가진 포인트를 조회할 수 있다.
- [ ] 전체 금액의 30%만큼 포인트를 사용할 수 있다.
- [ ] 주문완료시 결제 금액의 5%만큼 포인트가 적립된다.

## 주문
- [ ] 사용자는 장바구니에 담은 상품들을 주문할 수 있다.
- [ ] 이전 주문내역을 조회할 수 있다.

# API 설계

### Cart Item API

| HttpMethod | URL              | HttpStatus | Description           |
|------------|------------------|------------|-----------------------|
| GET        | /cart-items      | 200        | 카트 내 모든 상품을 조회한다.     |
| POST       | /cart-items      | 201        | 카트에 상품을 추가한다.         |
| PATCH      | /cart-items/{id} | 200        | 카트 내 특정 상품의 개수를 수정한다. |
| DELETE     | /cart-items/{id} | 204        | 카트 내 특정 상품을 제거한다.     |

### Product API

| HttpMethod | URL            | HttpStatus | Description    |
|------------|----------------|------------|----------------|
| GET        | /products      | 200        | 전체 상품을 조회한다.   |
| GET        | /products/{id} | 200        | 특정 상품을 조회한다.   |
| POST       | /products      | 201        | 상품을 추가한다.      |
| PUT        | /products/{id} | 200        | 상품 정보를 업데이트한다. |
| DELETE     | /products/{id} | 204        | 상품을 삭제한다.      |

### Member API

| HttpMethod | URL                       | HttpStatus | Description         |
|------------|---------------------------|------------|---------------------|
| GET        | /members/points           | 200        | 멤버의 포인트를 조회한다.      |
| GET        | /members/orders           | 200        | 멤버의 주문 목록을 조회한다.    |
| GET        | /members/orders/{orderId} | 200        | 멤버의 주문 상세 정보를 조회한다. |

### Pay API

| HttpMethod | URL  | HttpStatus | Description        |
|------------|------|------------|--------------------|
| POST       | /pay | 200        | 카트 내 선택한 상품을 주문한다. |

## 💋 결제 시나리오

1. 구매할 상품 조회
2. 사용자 포인트 조회
3. 포인트 사용 금액 결정
4. 결제

### `GET /members/points`

#### Request

Header

```yaml
{
  "Authorization": Basic ${ credentials }
}
```

#### Response

```
200 OK
```

Body

```json
{
  "points": 123
}
```
---
### `PATCH /cart-items/1`

#### Request

Body

```json
{
  "quantity": 5
}
```

#### Response

```
200 OK
```
---
### `POST /pay`

#### Request

Header

```yaml
{
  "Authorization": Basic ${ credentials }
}
```

Body

```json
{
  "cartItemIds": [
    {
      "cartItemId": 1
    },
    {
      "cartItemId": 3
    }
  ],
  "originalPrice": 4000,
  "points": 100
}
```

#### Response

```
200 OK
```

```json
{
  "orderId": 1
}
```
---
### `GET /members/orders`

#### Request

Header

```yaml
{
  "Authorization": Basic ${ credentials }
}
```

#### Response

```
200 OK
```

Body

```json
[
  {
    "orderId": 1,
    "orderPrice": 25000,
    "totalAmount": 2,
    "previewName": "PET보틀-정사각(370ml)"
  },
  {
    "orderId": 2,
    "orderPrice": 1400,
    "totalAmount": 3,
    "previewName": "[든든] 동원 스위트콘"
  }
]
```
---
### `GET /members/orders/{orderId}`

#### Request

Header

```yaml
{
  "Authorization": Basic ${ credentials }
}
```

#### Response

Body

```json
{
  "orderItems": [
    {
      "name": "[든든] 동원 스위트콘",
      "imageUrl": "http://image/test1.png",
      "count": 2,
      "price": 99800
    },
    {
      "name": "PET보틀-원형(500ml)",
      "imageUrl": "http://image/test2.png",
      "count": 3,
      "price": 84400
    }
  ],
  "originalPrice": 184400,
  "usedPoints": 1000,
  "orderPrice": 183400
}
```