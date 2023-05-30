# jwp-shopping-order

### 고민
1. product 와 cartItem의 관계 설정
1) product에 soft delete 적용 (isDelete라는 column 만들기) - FK O
2) cartItem가 product_id를 가지지만 FK 연결은 X
3) cartItem가 product_id를 가지고 FK 연결 O (product 날라가면 cartItem도 날리기)
뭐가 나을까?

2. 테스트 작성시 더미데이터 영향 받아도 되나?
data.sql 실행 안되게 해야하나?
