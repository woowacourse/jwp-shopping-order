# jwp-shopping-order

## 해야 할 일
- [x] DB 구조 짜기
- [ ] aws ec2 mysql db에 연결
  - 이때, test는 h2로 진행
- [ ] 기능 구현
  - 해당 내용은 아래 기능 목록에서 정리
- [ ] 에러 로깅 구현

## 기능 목록 및 API 명세
- [ ] 주문 관리에 대한 API 작성
  - [ ] 주문 생성
    - [ ] POST /orders 요청과 매핑
    - [ ] db에 저장한다
    - API 명세
      - 요청
        ```text
        POST /orders HTTP/1.1
        Content-Type: application/json
        ```
          
        ```json
        {
          "usedPoint": 3000,   // 사용된 포인트
          "products": [
            {
              "productId": 1,
              "Quantity": 100
            },
            {
              "productId": 2,
              "Quantity": 100
            }
          ]
        }
        ```
      - 응답
        ```text
        HTTP/1.1 201 Created
        Content-Type: application/json
        ```
        
  - [ ] 주문 목록 조회
    - [ ] GET /orders 요청과 매핑
    - [ ] db에서 특정 회원의 주문 목록을 조회한다
    - API 명세
      - 요청
        ```text
        GET /orders/?page=n HTTP/1.1
        Content-Type: application/json
        ```
      - 응답
        ```text
        HTTP/1.1 200 OK
        Content-Type: application/json
        ```
        ```json
        {
          "totalPages": 10,
          "currentPage": 1,
          "pageSize": 4,
          "contents": [
            {
              "orderId": 1,
              "payAmount": 10000, // 최종 결재 금액 (전체 - 포인트)
              "orderAt": "2024-10-23",
              "productName": "마우스",
            " productImageUrl": "http://example.com/chicken.jpg",
              "totalProductCount": 4
            },
            {
              "orderId": 1,
              "totalPrice": 10000,
              "orderAt": "2024-10-23",
              "productName": "마우스",
              "productImageUrl": "http://example.com/chicken.jpg",
              "totalProductCount": 4
            },
            {
              "orderId": 1,
              "totalPrice": 10000,
              "orderAt": "2024-10-23",
              "productName": "마우스",
              "productImageUrl": "http://example.com/chicken.jpg",
              "totalProductCount": 4
            }
          ]
        }
        ```
          

  - [ ] 단일 주문 조회
    - [ ] GET /orders/{orderId} 요청과 매핑
    - [ ] db에서 특정 회원의 특정 주문을 조회한다
    - API 명세
      - 요청
        ```text
        GET /orders/{orderId} HTTP/1.1
        Content-Type: application/json
        ```
      - 응답
        ```text
        HTTP/1.1 200 OK
        Content-Type: application/json
        ```
        ```json
        {
          "orderId": 1,
          "orderAt": "2023-05-26",
          "payAmount": 10000,  // 최종 결재 금액
          "usedPoint": 3000,   // 사용된 포인트
          "savedPoint": 4000,  // 적립된 포인트
          "products": [
            {
              "quantity": 5,
              "product": {
                "id": 1,
                "price": 10000,
                "name": "치킨",
                "imageUrl": "http://example.com/chicken.jpg"
              }
            },
            {
              "quantity": 1,
              "product": {
                "id": 2,
                "price": 20000,
                "name": "피자",
                "imageUrl": "http://example.com/pizza.jpg"
              }
            }
          ]
        }
        ```


  - [ ] 주문 취소
    - [ ] DELETE /orders/{orderId} 요청과 매핑
    - [ ] db에서 주문을 상태를 변경한다
    - API 명세
      - 요청
        ```text
        DELETE /orders/{orderId} HTTP/1.1
        Content-Type: application/json
        ```

      - 응답
        ```text
        HTTP/1.1 200 OK
        Content-Type: application/json
        ```


- [ ] 포인트 적립 기능
  - [ ] 포인트 생성
    - 주문과 함께 요청됨
    - 포인트 적립 규칙
      - 가격에 따른 일괄 퍼센트 포인트 적립
        - 5만원 미만 : 5%
        - 5만원 이상, 10만원 미만 : 8%
        - 10만원 이상 : 10%
      - 유효기간
        - 통일 90일 -> 3개월 후 1일로 설정 (2월 5일에 포인트 적립 -> 5월 1일까지 사용 가능)

  - [ ] 포인트 조회
    - [ ] GET /points 요청과 매핑
    - [ ] db에서 특정 회원의 포인트를 조회한다
    - API 명세
      - 요청
        ```text
        GET /points HTTP/1.1
        Content-Type: application/json
        ```

      - 응답
        ```text
        HTTP/1.1 200 OK
        Content-Type: application/json
        ```
        ```json
        {
          "currentPoint": 50000,    // 현재 포인트
          "toBeExpiredPoint": 1000  // 30일 내에 사라질 포인트
        }
        ```


  - [ ] 포인트 사용
    - 주문과 함께 요청됨
    - [ ] 사용 후 포인트가 남는 경우 -> 포인트 수정
    - [ ] 사용 후 포인트가 남지 않는 경우 -> 포인트 삭제
    - 포인트 사용 규칙
      - 유효기간이 얼마 남지 않은 포인트부터 차례로 사용
