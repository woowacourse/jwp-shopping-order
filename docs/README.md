# 구현 기능 목록

## 상품
- [ ] 상품을 추가할 수 있다.
- [ ] 상품을 조회할 수 있다.
- [ ] 상품을 수정할 수 있다.
- [ ] 상품을 삭제할 수 있다.

## 장바구니
- [ ] 장바구니에 상품을 추가할 수 있다.
- [ ] 장바구니에 상품을 조회할 수 있다.
- [ ] 장바구니에 상품의 수량을 수정할 수 있다.
- [ ] 장바구니에 상품을 삭제할 수 있다.

## 주문
- [ ] 사용자는 장바구니에 담은 상품들을 주문할 수 있다.
  - [ ] 사용자가 가진 포인트를 조회할 수 있다.
  - [ ] 전체 금액의 30%만큼 포인트를 사용할 수 있다.
  - [ ] 주문하면 결제 금액의 10%만큼 포인트가 적립된다.
- [ ] 이전 주문내역을 조회할 수 있다.

## 포인트
```http request
GET /members/points
Header: User Credential
```

> Response
> 200 OK
> ```json
> {
>   "points": 123
> }
> ```

## 결제
```http request
POST /cart-items/payment
Header {
	Authorization: Basic ${credentials}
}

Body {
	"cartItemIds": [
	   {"cartItemId": 1},
	   {"cartItemId": 3}, ...
	]
	"points": 100
}
```
> Response
> 200 OK
> ```json
> {
>   "redirect": "/orders/histories/1"
> }
> ```
