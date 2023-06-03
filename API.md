### API 명세

- 상품 (admin)

  | 기능 | HTTP Method | URL | HTTP Status |
            | --- | --- | --- | --- |
  | 상품 목록 조회 | get | /products | 200 |
  | 상품 상세 정보 조회 | get | /products/{id} | 200 |
  | 상품 추가 | post | /products | 201 |
  | 상품 정보 수정 | put | /products/{id} | 200 |
  | 상품 제거 | delete | /products/{id} | 204 |

- RESPONSE (상품 상세 정보 조회 `GET /cart-items/{id}`)
    ```http request
    {
        "name": "Example Product",
        "price": 100,
        "imageUrl": "http://example.com/image.jpg"
    }
    ```


- 장바구니

  | 기능 | HTTP Method | URL | HTTP Status |
      | --- | --- | --- | --- |
  | 장바구니 상품 목록 조회 | get | /cart-items | 200 |
  | 장바구니 상품 추가 | post | /cart-items | 201 |
  | 장바구니 상품 갯수 변경 | patch | /cart-items/{id} | 200 |
  | 장바구니 제거 | delete | /cart-items | 204 |

- REQUEST (장바구니 제거 `DELETE /cart-items`)
    ```http request
    {
        "cartItemIdList": [1, 2, 3]
    }
    ```

- 주문

  | 기능 | HTTP Method | URL | HTTP Status |
        | --- | --- | --- | --- |
  | 주문 목록 조회 | get | /orders | 200 |
  | 주문 상세 정보 조회 | get | /orders/{orderId} | 200 |
  | 주문하기 | post | /orders | 201 |
  | 주문 삭제 | delete | /orders/{orderId} | 204 |
  | 주문 취소 | patch | /orders/{orderId} | 200 |

- REQUEST (주문하기 `POST /orders`)
    ```http request
    {
        "cartItemIdList": [1, 2, 3],
            "totalPrice" : 10000,
            "deliveryFee" : 3000,
        "couponId": 15
    }
    ```

- RESPONSE (주문 목록 조회 `GET /orders`)
    ```http request 
    [
        {
            "orderId": 1,
            "products": [
                {
                    "id": 1,
                    "name": "치킨",
                    "imageUrl": "http://example.com/chicken.jpg",
                    "quantity": 4,
                    "totalPrice": 40000
                }
            ],
            "totalOrderPrice": 43000
        },
        {
            "orderId": 2,
            "products": [
                {
                    "id": 1,
                    "name": "치킨",
                    "imageUrl": "http://example.com/chicken.jpg",
                    "quantity": 4,
                    "totalPrice": 40000
                }
            ],
            "totalOrderPrice": 43000
        }
    ]
    ```

- RESPONSE (주문 내역 상세 조회 `GET /orders/{id}`)
    ```http request
    {
        "orderId" : 1,
        "products": [
            {
                "id": 1,
                "name": "치킨",
                "imageUrl": "http://example.com/chicken.jpg",
                "quantity": 4,
                "totalPrice" : 40000
            }
        ],
            "totalPrice" : 40000,
            "deliveryFee" : 3000,
            "coupon": {
                "id": 12,
                "name": "신규회원 3000원 할인",
                "priceDiscount" : 3000
            }
    }
    ```

- 쿠폰

  | 기능 | HTTP Method | URL | HTTP Status |
      | --- | --- | --- | --- |
  | 보유 쿠폰 목록 조회 | get | coupons | 200 |

- RESPONSE (보유 쿠폰 목록 조회 `GET /coupons`)
  ```http request
  [
     {
        "id": 12,
        "name": "신규회원 3000원 할인",
        "priceDiscount" : 3000
      }
  ]
  ```

### 에러 응답 코드

| 응답코드 | 에러 내용 |
| --- | --- |
| `400 BAD_REQUEST` | 유효하지 않은 요청 내용 (존재하지 않는 id / 유효하지 않은 quantity 값 / 장바구니 중복 생성) |
| `401 UNAUTORIZED` | 사용자 인증 실패 |
| `403 FORBIDDEN` | 사용자 권한 없음 (다른 사용자의 자원에 접근) |
| `409 CONFLICT` | 요청이 이미 존재하는 자원과 충돌 (주문 정보의 총 금액과 서버의 총 금액 불일치) |
| `500 INTERNAL_SERVER_ERROR` | 서버 에러 (클라이언트 잘못으로 인해 발생한 에러가 아님) |
