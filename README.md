# jwp-shopping-order

## Quick Start

```shell
cd docker

docker-compose -p order up -d
```

- 로컬에서 애플리케이션 실행을 위해서는 도커 컴포즈를 통해 DB 실행이 필요합니다.
- DB 실행 후 애플리케이션을 실행하면 default 프로필로 애플리케이션이 동작합니다.

## API Docs

```text
http://localhost:8080/swagger-ui/index.html
```

- Swagger을 사용하였기에 애플리케이션 실행 후 해당 경로에서 API 명세를 확인할 수 있습니다.

## 구현 기능

### 멤버

- 멤버 포인트 조회

### 상품

- 상품 목록 조회
- 상품 조회
- 상품 등록
- 상품 수정
- 상품 삭제

### 주문

- 주문 목록 조회
- 주문 조회
- 주문 진행

### 장바구니

- 장바구니 상품 목록 조회
- 장바구니 상품 목록 금액 조회
- 장바구니 상품 추가
- 장바구니 상품 수량 수정
- 장바구니 상품 목록 삭제
- 장바구니 상품 삭제
