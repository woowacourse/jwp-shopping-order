# jwp-shopping-order

# API 목록

## Product

| HttpMethod | URL                 | Description | 구현 여부 |
|------------|---------------------|-------------|-------|
| GET        | /products           | 전체 상품을 조회   | ✅     |
| GET        | /products/{id}      | 상품 상세 조회    | ✅     |
| GET        | /products?ids={ids} | 특정 상품 조회    | ✅     |
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
| GET        | /orders      | 주문 목록 조회    | ✅     |
| GET        | /orders/{id} | 주문 상세 조회    | ✅     |
| POST       | /orders      | 주문 등록       | ✅     |

## 기능 추가 목록

### 상품

- [x] 특정 상품 상세 조회
    - 상품 여러개를 지정하여 요청하는 경우(ex.최근 10개 상품 조회)

### 주문

- [x] 주문 도메인 추가
- [x] 주문 등록 API
    - [x] 요청한 사용자의 장바구니에 저장된 상품인지 검증
    - [x] 요청된 금액과 실제 계산되어야 하는 금액 교차 검증
    - [x] 할인 정책 반영

- [x] 주문 목록 조회 API
- [x] 주문 상세 조회 API

