## API 명세서

추가 구현한 기능 : 쿠폰 발급 및 적용

### 장바구니에 담은 상품 주문

- Request
    - Header : Authorization
    - Body
        - cartItem의 모든 정보 List
        - 사용한 쿠폰 ID 배열
    - Example

      `POST /orders`

        ```json
        {
            "cartItems": [
                {
                    "id": 7,
                    "quantity": 5,
                    "product": {
                        "id": 1,
                        "price": 10000,
                        "name": "치킨",
                        "imageUrl": "http://example.com/chicken.jpg"
                    }
                },
                {
                    "id": 8,
                    "quantity": 2,
                    "product": {
                        "id": 2,
                        "price": 24000,
                        "name": "피자",
                        "imageUrl": "http://example.com/pizza.jpg"
                    }
                }
            ],
            "couponIds": [
                1
            ],
        		"deliveryFee": 3000
        }
        ```

- Response
    - Body : 주문 ID
    - Example
        - 성공 : 201 Created

            ```json
            {
               "orderId": 3
            }
            ```

        - 실패 : 400 Bad Request / 500 Internal Server Error

### 사용자 별 주문 목록

- 용어 정리
    - originalPrice: 상품 가격들만 다 더한거 (배송비 포함 x)
    - actualPrice: 쿠폰 적용된 가격
    - deliveryFee: 배송비
- Request
    - Header : Authorization

  `GET /orders`

- Response
    - 사용자의 주문 List
        - 주문 → 주문에 대한 모든 정보 반환
- Example

    ```json
    {
      "orders": [
            {
                "id": 1,
                "originalPrice": 100000,
                "actualPrice": 90000,
                "deliveryFee": 3000,
                "cartItems": [
                        {
                            "id": 7,
                            "quantity": 5,
                            "product": {
                            "id": 1,
                            "price": 10000,
                            "name": "치킨",
                            "imageUrl": "http://example.com/chicken.jpg"
                        }
                        },
                        {
                            "id": 8,
                            "quantity": 2,
                            "product": {
                            "id": 2,
                            "price": 24000,
                            "name": "피자",
                            "imageUrl": "http://example.com/pizza.jpg"
                        }
                        },
                ]
            },
            {
                "id": 2,
                "originalPrice": 200000,
                "actualPrice": 180000,
                "deliveryFee": 3000,
                "cartItems": [
                        {
                            "id": 9,
                            "quantity": 3,
                            "product": {
                            "id": 1,
                            "price": 10000,
                            "name": "치킨",
                            "imageUrl": "http://example.com/chicken.jpg"
                        }
                        },
                        {
                            "id": 10,
                            "quantity": 1,
                            "product": {
                            "id": 2,
                            "price": 24000,
                            "name": "피자",
                            "imageUrl": "http://example.com/pizza.jpg"
                        }
                        },
                ]
            }
        ]
    }
    ```


### 특정 주문의 상세 정보

- Request
    - Header : Authorization

  GET /orders/{orderId}

- Response
    - 특정 주문에 대한 모든 정보 반환
    - Example

    ```json
    {
        "id": 1,
        "originalPrice": 100000,
        "actualPrice": 90000,
        "deliveryFee": 3000,
        "cartItems": [
            {
                "id": 7,
                "quantity": 5,
                "product": {
                    "id": 1,
                    "price": 10000,
                    "name": "치킨",
                    "imageUrl": "http://example.com/chicken.jpg"
                }
            },
            {
                "id": 8,
                "quantity": 2,
                "product": {
                    "id": 2,
                    "price": 24000,
                    "name": "피자",
                    "imageUrl": "http://example.com/pizza.jpg"
                }
            }
        ]
    }
    ```


### 쿠폰 발급

- Request
    - Header : Authorization
    - Body
        - 쿠폰 ID (배너 ID를 받아서 매핑시켜주는 게, 쿠폰의 보안 상 나을 수 있음)
    - Example

      `POST coupons/me`

        ```json
        {
            "couponId": 1
        }
        ```

- Response
    - 성공 : 200
    - 실패 : 400
    - Body 없음

### 쿠폰 조회

→ 배너에 보여줄 (등록하기 위해) 쿠폰 정보 조회

- Request

  `GET /coupons`

- Response
    - (전체) 쿠폰 List
    - Example

        ```json
        {
            "coupons": [
                {
                    "id": 1
                    "type": "percent",
                    "amount": 10,
                    "name": "신규 회원 환영 쿠폰" 
                },
                {
                    "id": 2,
                    "type": "amount",
                    "amount": 3000,
                    "name": "고정 금액 할인 쿠폰"
                }
            ]
        }
        ```


### 사용자가 보유한 쿠폰 조회

- Request
    - Header : Authorization

  `GET /coupons/me`

- Response
    - 사용자가 보유한 쿠폰 List
- Example

    ```json
    {
        "coupons": [
            {
                 "id": 1,
                 "type": "percent",
                 "amount": 10,
                 "name": "신규 회원 환영 쿠폰" 
            }
        ]
    }
    ```


## 쿠폰 발급 API

- Request

  `POST /coupons`

    ```json
    {
        "type": "percent",
        "amount": 10,
        "name": "신규 회원 환영 쿠폰" 
    }
    ```

- Response

  status 201
    ```json
    {
        "id": 1,
        "type": "percent",
        "amount": 10,
        "name": "신규 회원 환영 쿠폰" 
    }
    ```

## 장바구니 상품 여러개 delete

- Request

`DELETE /cart-items`

```json
{
    "cartItemIds":[1,2,3]
}
```

- Response

  status 204
