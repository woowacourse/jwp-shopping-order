## 2단계 시나리오
- [x] 장바구니에서 상품들을 골라 '결제하기' 버튼을 눌러 결제 창으로 넘어간다.
  - [x] 이때 서버에서 해당 사용자의 포인트를 넘겨준다.
    - 요청
        ```text
            Authorization: Basic ${credentials}
            GET /members/point
        ```
    - 응답
        ```text
            200 OK
            {
              "point" : 10000
            }
        ```
- [x] 사용자는 결제창에서 결제 금액을 확인하고, 사용할 포인트를 입력한 후에 '결제하기' 버튼을 눌러 결제를 완료한다.
    - [x] 총 결제 금액이 5만원 이상이라면, 배송비를 무료로 해준다. (배송비는 3000원) 
    - [x] 포인트는 숫자로 들어와야 한다.
    - [x] 포인트는 음수값으로 들어올 수 없다.
    - [x] 자신이 보유한 포인트를 초과하여 사용할 수 없다. 
    - [x] 주문 금액을 초과하는 포인트 사용은 불가하다.
      - 요청
        ```text
            Authorization: Basic ${credentials}
            POST /orders
            {
              "cartItemIds" : [1, 3, 5, 6],
              "point" : 1500      
            }
        ```
      - 응답
          ```text
             201 CREATED
             Location : orders/3
          ```
- [ ] 사용자는 전체 주문 내역을 조회할 수 있다.
  - 요청
    ```text
        Authorization: Basic ${credentials}
        GET /orders
    ```
  - 응답
    ```text
        [
          {
            "orderId" : 1,
            "totalPrice" : 30000,
            "usedPoint" : 2000,
            "orderedAt" : 2023/05/26 02:25:36,
            "products" : [
              {
                "name" : "치킨",
                "price" : 10000,
                "image_url" : "chickenUrl",
                "quantity" : 3
              },
              ...
            ]
          },
          ... 
        ]
    ```
- [ ] 사용자는 단일 상세 주문 내역을 조회할 수 있다.
  - 요청
     ```text
         Authorization: Basic ${credentials}
         GET /orders/3
     ```
  - 응답
    ```text
       {
         "orderId" : 3,
         "totalPrice" : 30000,
         "usedPoint" : 2000,
         "orderedAt" : 2023/05/26 02:25:36,
         "products" : [
           {
             "name" : "치킨",
             "price" : 10000,
             "image_url" : "chickenUrl",
             "quantity" : 3
           },
           ...
         ]
       },
    ```
