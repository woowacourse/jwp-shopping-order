# 기능 목록

## 장바구니에 담은 상품들 주문하기
- [x] POST 요청, URI = "/orders"인 API 작성
- [x] OrderCreateRequest 생성
- [x] service에서 domain 객체 생성 (Cart)
  - [x] CartItem의 id에 대한 List를 가지고 있다
  - [x] request안의 금액들이 올바른지 검증
- [x] domain으로 dao에 요청 전송
- [x] 주문한 상품들의 총액을 계산
- [x] 배송비 계산
- [x] 상품 할인 계산
- [x] dao에서 영속화 작업 수행
  - [x] 주문한 상품들은 ordered_item에 추가
  - [x] 주문한 상품들은 장바구니에서 삭제


## 특정 멤버의 주문 전체 목록 조회하기
- [x] GET 요청, URI = "/members/{memberId}/orders"인 API 작성
- [x] service에서 domain 객체 생성 (Orders)
  - [x] Order들의 List를 가지고 있다 
- [x] domain으로 dao에 요청 전송
- [] dao에서 영속화 작업 수행
  - [] 전체 주문 목록을 조회하는 쿼리문 작성
- [] OrdersResponse 생성


## 특정 멤버의 특정 주문 조회하기
- [x] GET 요청, URI = "/members/{memberId}/orders/{orderId}"인 API 작성
- [x] service에서 dao로 요청 전송
- [x] domain으로 dao에 요청 전송
- [x] dao에서 영속화 작업 수행
  - [x] 특정 주문의 정보들을 조회하는 쿼리문 작성
- [x] OrderResponse 생성


## 멤버 등급 조회하기
- [] GET 요청, URI = "/member"인 API 작성
- [] service에서 domain 객체 생성(Member)
  - [] rank와 discountRate를 가지고 있다
- [] domain에서 dao로 영속화 작업 수행
  - [] 멤버의 등급과 할인율을 조회하는 쿼리문 작성
- [] MemberResponse 작성 

## 상품 전체 조회하기
- [] GET 요청, URI = "/products"인 API 작성
- [] service에서 domain 객체 생성(Products)
  - [] Product의 List를 가지고 있다 
- [] domain으로 dao에 요청 전송
- [] dao에서 영속화 작업 수행
  - [] 모든 상품들을 조회하는 쿼리문 작성 
- [] ProductsResponse 작성


## 주디가 했던 고민들
1. 현재는 Domain과 Entity가 분리 되어있지 않은 상황같습니다. 개인적으로는 Domain과 Entity를 분리하여 DB에 data를 넣고 뺄 때는 entity를 사용하고 그렇지 않을 때는 domain을 사용했었는데, 리뷰어님은 어떻게 domain과 entity의 역할을 구분지으셨나요?
2. 
