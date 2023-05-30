# 기능 목록

## 기존 기능

### 상품 관련
- 새로운 상품을 등록할 수 있다.
  - 상품의 이름을 지정한다.
    - 상품 이름은 중복될 수 있다.
  - 상품의 가격을 지정한다.
  - 상품의 이미지 파일을 추가한다.
- 등록된 상품을 검색할 수 있다.
  - 모든 상품을 검색할 수 있다.
  - Id 를 통해 하나의 상품을 검색할 수 있다.
    - 해당 Id의 상품이 없다면 요청은 실패한다.
- 등록된 상품을 업데이트할 수 있다.
  - 상품의 이름, 가격, 이미지 URL 을 업데이트 할 수 있다.
    - 해당 Id의 상품이 없다면 요청은 실패한다.
- 등록되 상품을 삭제할 수 있다.
  - 해당 Id의 상품이 없다면 요청은 실패한다.
