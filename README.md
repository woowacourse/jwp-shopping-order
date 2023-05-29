# jwp-shopping-order

# API

| 기능 | 메소드 | url | 구현 여부 |
| --- | --- | --- |-------|
| 주문하기 | POST | ~/orders | o     |
| 주문 목록 조회 | GET | ~/orders | o     |
| 주문 상세 보기 | GET | ~/orders/{id} | x     |
| 주문 확정 | PATCH | ~/orders/{id}/confirm | x     |
| 주문 취소 | DELETE | ~/orders/{id} | x     |
| 유저 쿠폰 발급 | POST | ~/users/coupons | x     |
| 모든 쿠폰 조회 | GET | ~/coupons | x     |
| 유저 쿠폰 조회 | GET | ~/users/coupons | x     |

- **[POST] 주문하기 (~/orders)**

  ### **Request**

    - header

    ```
    Authorization: Basic ...
    ```

    - body

    ```json
    {
            "cart_product_ids" : [1, 2, 3],
            "original_price" : 24700,
            "discount_price" : 22000,
            "coupon_id" : 1
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
        "order_products" : [
          {
            "products": {
              "id" : 1,
              "name" : "치킨",
              "image_url" : "url",
              "price" : 20000
             },
         "quantity" : 3
          },
          {
            "products" : {
               "id" : 2,
               "name" : "피자",
               "image_url" : "url",
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
                "image_url" : "url",
                "price" : 20000
              },
            "quantity" : 1
          }
        ],
        "confirm_state" : false
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
              "image_url" : "url",
              "price" : 20000
            },
          "quantity" : 1
        }
        ],
      "original_price" : 15000,
      "discount_price" : 12000,
      "confirm_state" : false,
      "coupon" : {
          "id" : 1,
          "name" : "5월의 달 20% 할인 쿠폰",
          "discount_type" : "percentage",
          "discount_rate" : 0.2,
          "discount_amount" : 0,
          "minimum_price" : 50000
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
    		"discount_type" : "percentage",
    		"discount_rate" : 0.2,
    		"discount_amount" : 0
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
    		"discount_type" : "percentage",
    		"discount_rate" : 0.2,
    		"discount_amount" : 0, // 원래가격 * (1-discount_rate) + discount_amount
    		"issuable" : true
    	},
    	{
    		"coupon_id" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰",
    		"discount_type" : "deduction",
    		"discount_rate" : 0.0,
    		"discount_amount" : 1000,
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
    		"discount_type" : "percentage",
    		"discount_rate" : 0.2,
    		"discount_amount" : 0,
    		"minimum_price" : 50000
    	},
    	{
    		"coupon_id" : 2,
    		"name" : "5월의 달 1000원 할인 쿠폰"
    		"discount_type" : "deduction",
    		"discount_rate" : 0.0,
    		"discount_amount" : 1000,
    		"minimun_price" : 0
    	}
    ]
    ```
