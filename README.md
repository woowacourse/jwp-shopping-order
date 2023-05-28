# jwp-shopping-order

# API 목록

## Product
| HttpMethod | URL                 | Description | 구현 여부 |
|------------|---------------------|-------------|-------|
| GET        | /products           | 전체 상품을 조회   | ✅     |
| GET        | /products/{id}      | 상품 상세 조회    | ✅     |
| GET        | /products?ids={ids} | 특정 상품 조회    |       |
| POST       | /products           | 상품 추가한다     | ✅     |
| PUT        | /products/{id}      | 상품 정보 수정    | ✅     |
| DELETE     | /products/{id}      | 상품 삭제       | ✅     |

## Cart-item
| HttpMethod | URL              | Description     | 구현 여부 |
|------------|------------------|-----------------|-------|
| GET        | /cart-items      | 장바구니 내 상품 조회    | ✅     |
| POST       | /cart-items      | 장바구니에 상품을 추가    | ✅     |
| PATCH      | /cart-items/{id} | 장바구니내 상품 개수 변경. | ✅     |
| DELETE     | /cart-items/{id} | 장바구니에서 제거       | ✅     |

## Order
| HttpMethod | URL          | Description | 구현 여부 |
|------------|--------------|-------------|-------|
| GET        | /orders      | 주문 목록 조회    |       |
| GET        | /orders/{id} | 주문 상세 조회    |       |
| POST       | /orders      | 주문 등록       |       |

