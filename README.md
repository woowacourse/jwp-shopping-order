# jwp-shopping-order

# API

| 기능 | 메소드 | url | 구현 여부 |
| --- | --- | --- |-------|
| 주문하기 | POST | ~/orders | o     |
| 주문 목록 조회 | GET | ~/orders | o     |
| 주문 상세 보기 | GET | ~/orders/{id} | o     |
| 주문 확정 | PATCH | ~/orders/{id}/confirm | o     |
| 주문 취소 | DELETE | ~/orders/{id} | o     |
| 유저 쿠폰 발급 | POST | ~/users/coupons | o     |
| 모든 쿠폰 조회 | GET | ~/coupons | o     |
| 유저 쿠폰 조회 | GET | ~/users/coupons | o     |

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
        Location: orders/{id}
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
            "products": {
              "id" : 1,
              "name" : "치킨",
              "imageUrl" : "url",
              "price" : 20000
             },
         "quantity" : 3
          },
          {
            "products" : {
               "id" : 2,
               "name" : "피자",
               "imageUrl" : "url",
               "price" : 30000
            },
          "quantity" : 2
          }
        ],
        "confirm_state" : true
      },
      {
        "id" : 2,
        "order_products" : [
          {
            "products":{
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
      "order_products" : [
        {
          "products" : {
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
    		"issuable" : true
    	},
    	{
    		"couponId" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰",
    		"discountType" : "deduction",
    		"discountRate" : 0.0,
    		"discountAmount" : 1000,
    		"issuable" : false
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
    		"coupon_id" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰",
    		"discountType" : "deduction",
    		"discountRate" : 0.0,
    		"discountAmount" : 1000,
    		"minimumPrice" : 0
    	}
    ]
    ```
