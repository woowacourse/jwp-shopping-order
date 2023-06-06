# jwp-shopping-order

## [2단계]

### API 명세서

| Method | URI                 | Description        |
|--------|---------------------|--------------------|
| POST   | /cart-items/payment | 장바구니에 담은 상품을 주문한다. |

#### [POST] /cart-items/payment

Description: 장바구니에 담은 상품을 주문한다.

Header

- `Authorization: Basic ${credentials}`

Request Body

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

Response

- Status Code: 201 (CREATED)
- Return: `redirect:/orders/histores/1`

### 기능 요구사항

- [ ] 장바구니에 담은 상품을 주문할 수 있다.
- [ ] 상품 주문 시 현실 세계의 쇼핑 서비스가 제공하는 재화 관련 요소를 최소 1가지 이상 추가한다.
    - 재화 관련 요소: 포인트
    - [ ] 사용자는 결제 전 자신이 보유한 포인트를 사용할 수 있다.
    - [ ] 포인트는 사용자가 구매한 금액의 5%가 적립된다.
- [ ] 사용자 별로 주문 목록을 확인할 수 있다.
- [ ] 특정 주문의 상세 정보를 확인할 수 있다.
