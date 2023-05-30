# 장바구니 미션 (협업)

## 💋 API 설계

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
| POST       | /products      | 201        | 상품을 추가한다.      |
| PUT        | /products/{id} | 200        | 상품 정보를 업데이트한다. |
| DELETE     | /products/{id} | 204        | 상품을 삭제한다.      |

### Member API

| HttpMethod | URL             | HttpStatus | Description    |
|------------|-----------------|------------|----------------|
| GET        | /members/points | 200        | 멤버의 포인트를 조회한다. |

### Order API

| HttpMethod | URL                 | HttpStatus | Description        |
|------------|---------------------|------------|--------------------|
| POST       | /cart-items/payment | 201        | 카트 내 선택한 상품을 주문한다. |

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

### `POST /cart-items/payment`

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
  "points": 100
}
```

#### Response

```
201 CREATED  /orders/histories/1
```
