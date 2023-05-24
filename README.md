# jwp-shopping-order

## API 설계

| HttpMethod | URL              | HttpStatus | Description           |
|------------|------------------|------------|-----------------------|
| GET        | /cart-items      | 200        | 카트 내 모든 상품을 조회한다.     |
| POST       | /cart-items      | 201        | 카트에 상품을 추가한다.         |
| PATCH      | /cart-items/{id} | 200        | 카트 내 특정 상품의 개수를 수정한다. |
| DELETE     | /cart-items/{id} | 204        | 카트 내 특정 상품을 제거한다.     |
| GET        | /products        | 200        | 전체 상품을 조회한다.          |
| GET        | /products/{id}   | 200        | 특정 상품을 조회한다.          |
| POST       | /products        | 201        | 상품을 추가한다.             |
| POST       | /products        | 201        | 상품을 추가한다.             |
| PUT        | /products/{id}   | 200        | 상품 정보를 업데이트한다.        |
| DELETE     | /products/{id}   | 204        | 상품을 삭제한다.             |
