# API 명세서

| 기능 | 메소드 | url |
| --- | --- | --- |
| 주문하기 | POST | ~/orders |
| 주문 목록 조회 | GET | ~/orders |
| 주문 상세 보기 | GET | ~/orders/{id} |
| 주문 확정 | PATCH | ~/orders/{id}/confirm |
| 주문 취소 | DELETE | ~/orders/{id} |
| 유저 쿠폰 발급 | POST | ~/users/coupons |
| 모든 쿠폰 조회 | GET | ~/coupons |
| 유저 쿠폰 조회 | GET | ~/users/coupons |

## [POST] 주문하기 (~/orders)

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
```text
Location: /orders/{id}
```

## [GET] 주문 목록 가져오기 (~/orders)

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
              "price" : 20000,
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
          "product": {
            "id" : 2,
            "name" : "피자",
            "imageUrl" : "url",
            "price" : 30000
          },
          "quantity" : 2
        }
      ],
      "confirmState" : false
  }
]
```

## [GET] 주문 상세 보기 (~/orders/{id})

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

## [PATCH] 주문 확정 (~/orders/{id}/confirm)

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


## [DELETE] 주문 취소 (~/orders/{id})
### **Request**

- header

```
Authorization: Basic ...
```

### **Response**

- status: 204(No Content)

## [POST] 유저 쿠폰 발급(~/users/coupons)

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


## [GET] 모든 쿠폰 조회(~/coupons)

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


## [GET] 유저 쿠폰 조회 (~/users/coupons)
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
		"name" : "5월의 달 1000원 할인 쿠폰"
		"discountType" : "deduction",
		"discountRate" : 0.0,
		"discountAmount" : 1000,
		"minimumPrice" : 0
	}
]
```

# 기능 목록

## 쿠폰(Coupon)
* [x] 쿠폰을 적용한 할인값 구하기

## 주문(Order)

* [x] 주문 신청
  * [x] 주문한 상품들의 총 가격 구하기
  * [x] 주문한 상품들의 할인 가격 구하기
  * [x] 주문한 상품들을 장바구니에서 제거

* [x] 주문 확정
  * [x] 주문 확정시 사용 가능한 쿠폰을 유저에게 발급

* [x] 주문 리스트 조회
* [x] 주문 상세 조회

* [x] 주문 취소
  * [x] 주문시 사용했던 쿠폰 재발급

* [x] 모든 쿠폰 조회
* [x] 유저 쿠폰 발급
  * 유저가 발급 가능한 쿠폰인지 검증(이미 소유중인 쿠폰이면 발급 불가능)
* [x] 유저 쿠폰 조회

