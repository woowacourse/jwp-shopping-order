# jwp-shopping-order

## 공통
- [x] 인증이 실패했다면 401 응답한다.
  - [x] Authorization 헤더가 없는 경우
  - [x] Basic이 아닌 경우
  - [x] 이메일이 없는 경우
  - [x] 비밀번호가 일치하지 않는 경우

## 장바구니에 담긴 물건 주문하기
- [x] 주문 요청으로 들어온 상품들을 엔티티로 만든다. 
  - [x] 장바구니에 id가 존재하지 않는다면 404 응답한다.
  - [x] 전체 수량이 0 이하 100개 이상이면 400 응답한다.
- [x] 입력된 가격이 정확한지 확인한다. 
  - [x] 30000원 넘으면 2000원 할인, 50000원 이상 5000원 할인 적용되었는지 확인한다.
  - [x] 일치하지 않는다면 400 응답한다. 
- [x] 주문을 새롭게 생성한다.
  - [x] 장바구니에서 해당 상품들을 삭제한다.
  - [x] 주문을 저장한다.
- [x] 주문 상세 조회를 할 수 있는 uri와 함께 201 응답한다. 

## 주문 상세 조회하기
- [x] 주문이 사용자의 주문이 아니라면 403 응답한다.
- [x] 주문이 존재하지 않으면 404 응답한다.
- [x] 주문을 엔티티로 찾아온다.
- [x] 주문 정보를 200 응답한다. 

## 주문 목록 조회하기 
- [ ] 특정 유저의 모든 주문 목록을 받아온다. 
- [ ] 각 주문에서 구매 첫 상품을 골라서 상품 이름, 이미지 URL 등으로 응답을 구성하여 200 응답한다.

## 상품 여러 개 조회하기
- [ ] 쿼리 스트링을 파싱하여 여러 상품을 한 번에 조회한 결과를 200 응답한다.
- [ ] 아이디가 존재하지 않을 경우 404 응답한다. 
