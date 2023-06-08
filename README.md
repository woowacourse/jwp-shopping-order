# jwp-shopping-order

## 요구사항 정리

### 주문
- [x] 장바구니에 담은 상품을 주문 할 수 있다.
    - [x] 주문한 상품은 장바구니에서 삭제된다.
    - [x] 쿠폰 사용시 쿠폰이 사용불가로 변경된다.
- [x] 주문 목록을 확인 할 수 있다.
- [x] 특정 주문의 상세 정보를 확인 할 수 있다.
- [x] 주문 확정을 할 수 있다.
    - [x] 주문 확정 시 쿠폰이 발급된다.
- [x] 주문을 취소 할 수 있다.
    - [x] 주문 취소 시 쿠폰이 복구 된다.

### 쿠폰
- [x] 사용자가 특정 쿠폰을 발급한다.
- [x] 발급할 수 있는 모든 쿠폰을 조회한다.
    - [x] 이미 발급한 쿠폰과 발급하지 않은 쿠폰을 분리한다.
- [x] 사용자의 쿠폰을 조회할 수 있다.
- [x] 쿠폰 사용 시 할인이 된다.
    - [x] 쿠폰 사용 조건이 있다.
        - [x] 특정 금액 이상 시 사용 가능
    - [x] 비율 할인
    - [x] 금액 할인

# API 명세
- **[POST] 주문하기 (~/orders)**

  ### **Request**

  - header

    ```
    Authorization: Basic ...
    ```

  - body

    ```json
    {
    	"selectCartIds" : [1, 2, 3],
        "couponId" : 1
    }
    ```

  ### **Response**

  - status: 201(Created)
  - header

      ```
      Location: /orders/{id}
      ```


- **[GET] 주문 목록 가져오기 (~/orders)**

  ### **Request**

  - header

    ```
    Authorization: Basic ...
    ```

  ### **Response**

  - status: 200(Ok)
  - body

    ```json
    [
      {
          "id" : 1,
             "orderProducts" : [
                        {
                            "product": {
                                "id" : 1,
                                "name" : "치킨",
                                "imageUrl" : "url",
                                "price" : 20000
                         },
                        "quantity" : 3
                        },
                        {
                            "product" : {
                                 "id" : 2,
                                 "name" : "피자",
                                 "imageUrl" : "url",
                                 "price" : 30000
                        },
                         "quantity" : 2
                        }
             ],
          "confirmState" : true
        },
        {
           "id" : 2,
                "orderProducts" : [
                           {
                               "product":{
                                     "id" : 1,
                                     "name" : "치킨",
                                     "imageUrl" : "url",
                                     "price" : 20000
                           },
                            "quantity" : 1
                            }
                  ],
        "confirmState" : false
        }
    ]
    ```


- **[GET] 주문 상세 보기 (~/orders/{id})**

  ### **Request**

  - header

    ```
    Authorization: Basic ...
    ```

  ### **Response**

  - status: 200(Ok)
  - body

    ```json
    {
           "id" : 2,
               "orderProducts" : [
                  {
    	            "product" : {
    			            "id" : 1,
    			            "name" : "치킨",
    			            "imageUrl" : "url",
    			            "price" : 20000
    			    },
                   "quantity" : 1
                  }
               ],
           "originalPrice" : 15000,
    	   "discountPrice" : 12000,
           "confirmState" : false,
    			"coupon" : {
    			      "id" : 1,
    			      "name" : "5월의 달 20% 할인 쿠폰",
    				  "discountType" : "percentage",
    				  "discountRate" : 0.2,
    				  "discountAmount" : 0,
    				  "minimumPrice" : 50000
    			}	
    }
    ```


- **[PATCH] 주문 확정 → 쿠폰 발급 (~/orders/{id}/confirm)**

  ### **Request**

  - header

    ```
    Authorization: Basic ...
    ```

  ### **Response**

  - status : 200 (Ok)
  - header

    ```json
    {
          "coupon" :{
    		"id" : 1,
    		"name" : "5월의 달 20% 할인 쿠폰",
    		"discountType" : "percentage",
    		"discountRate" : 0.2,
    		"discountAmount" : 0
    	}
    }
    ```


- **[DELETE] 주문 취소 (~/orders/{id})**

  ### **Request**

  - header

    ```
    Authorization: Basic ...
    ```

  ### **Response**

  - status: 204(No Content)

- **[POST] 유저 쿠폰 발급(~/users/coupons)**

  ### Request

  - header

    ```
    Authorization: Basic ...
    ```

  - body

    ```json
    {
    	"id" : 1
    }
    ```

  ### Response

  - status: 201(Created)

- **[GET] 모든 쿠폰 조회(~/coupons)**

  ### Request

  - header

    ```
    Authorization: Basic ...
    ```

  ### Response

  - status: 200(Ok)
  - Body

    ```json
    [
    	{
    		"id" : 1,
    		"name" : "5월의 달 20% 할인 쿠폰",
    		"discountType" : "percentage",
    		"discountRate" : 0.2,
    		"discountAmount" : 0,
    		"minimumPrice" : 0,
    		"issuable" : true
    	},
    	{
    		"id" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰",
    		"discountType" : "deduction",
    		"discountRate" : 0.0,
    		"discountAmount" : 1000,
    		"minimumPrice" : 0,
    		"issuable" : false
    	},
    	{
    		"id" : 3,
    		"name" : "50000원 이상 구매시 20% 할인 쿠폰",
    		"discountType" : "percentage",
    		"discountRate" : 0.2,
    		"discountAmount" : 0,
    		"minimumPrice" : 50000,
    		"issuable" : true
    	}
    ]
    ```


- **[GET] 유저 쿠폰 조회 (~/users/coupons)**

  ### Request

  - header

    ```
    Authorization: Basic ...
    ```

  ### Response

  - status: 200(Ok)
  - Body

    ```json
    [
    	{
    		"id" : 1,
    		"name" : "50000원 이상 구매시 20% 할인 쿠폰",
    		"discountType" : "percentage",
    		"discountRate" : 0.2,
    		"discountAmount" : 0,
    		"minimumPrice" : 50000
    	},
    	{
    		"id" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰",
    		"discountType" : "deduction",
    		"discountRate" : 0.0,
    		"discountAmount" : 1000,
    		"minimumPrice" : 0
    	}
    ]
    ```
