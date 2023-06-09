# jwp-shopping-order

## 토큰 생성하기
1. JWT 토큰 생성 클래스 만들기
   - 사용자의 인증 정보를 기반으로 JWT 토큰을 생성하고 반환하는 역할을 수행 
   - 토큰 생성을 위해 필요한 정보는 
     - 사용자의 식별자, 역할 등을 포함.
   - 일반적으로는 Java 라이브러리 중 하나인 jjwt를 사용하여 JWT 토큰을 생성
   
2. 인증 및 인가 인터셉터 - 리졸버 클래스 만들기
   - JWT 토큰을 검증하고 사용자의 인증 및 인가를 처리하는 클래스
   - 이 인터셉터 클래스는 클라이언트로부터 전달받은 JWT 토큰을 파싱하고 유효성을 검증
   - 유효한 토큰인 경우, 사용자를 인증하고 요청에 대한 인가를 수행
   - HttpServletRequest에서 헤더 또는 요청 파라미터 등에서 토큰을 추출하고, 
   - jjwt 라이브러리를 사용하여 토큰을 검증

3. 인증 및 인가 설정 클래스 만들기
   - 인증 및 인가에 대한 구성을 수행

4. 사용자 서비스 및 인증 매니저 클래스를 구현해야 합니다.
사용자 서비스 클래스는 사용자의 식별자에 기반하여 사용자 정보를 가져오는 역할을 수행합니다.
인증 매니저 클래스는 사용

# 도메인
1. OrderItemsFactory (또는 Cart)
    - 멤버의 장바구니 Cart : List<CartItem>
    - [x] 주문하려는 멤버와 Product상품과 quantity양이 존재 및 일치하는지 확인하고
    - [x] 주문하려는 List<CartItem>을 List<OrderItem>으로 변환하기
      :네이밍은 createOrderItems (장바구니에서 주문할 상품들 생성및 반환)
2. OrderItem
    - [x] 필드
      - orderItemId
      - memberId
      - Product
      - quantity

3. Order
    - 주문Id
    - 회원Id
    - createdAt 주문 생성시간
    - orderItems 주문 상품들
   
    - 배송비 delivery_fee
    - 할인 금액 discount_price
    - [x] 상품 금액 product_price
    - [x] 총 금액 total_price

4. DiscountCalculator (인터페이스)

5. DeliveryFeeCalculator (인터페이스)

# 추가할 API

## 1. 주문 API
### Request
- method : POST
- url : /orders
- Authorization Header : 멤버 인증 정보 ( Basic )
- Body
```json
{
	"order_items" : [
		{
			"id": 1, //상품 ID
			"quantity": 4, //상품 수량
		},
		...
		{
			"id": 3,
			"quantity": 2,
		},
	]
	"order_time" : "형식 협의"
}
```

### Response
- status code : created ( 201 )
- Location header : /orders/{orderId}
- Body
    ```json
    {
    	order_id : 1,
    	items : [
    			{
    			    "id": 1, //주문상품 ID
    			    "quantity": 5, //주문 상품 수량
    			    "product": {
    			        "id": 1, //상품 ID
    			        "price": 10000,
    			        "name": "치킨",
    			        "imageUrl": "http://example.com/chicken.jpg"
    			    }
    			},
    			...
    	],
    	product_price : 30000, //상품들의 총 가격
    	discount_price : 3000, //할인 가격
    	delivery_fee : 3000 //배달 가격
    	total_price : 30000 //총 가격
    }
    ```


### 구현하기

1. 컨트롤러 매개변수
    - 멤버 객체 
    - [] OrderAddRequest 객체
      - List<OrderItemRequest> orderItems
      - String createdAt
    - [] OrderItemRequest 객체
      - Long product_id, Integer quantity
          
2. 서비스 주문 추가하기
   - 멤버 id로 모든 cartItem 불러오기
   - Cart 객체 만들기 (List<CartItem> 일급 컬렉션)
   - Request(productId,quantity)로 주문 목록 CartItem 만들기
   - Cart 객체에서 검증 후, OrderItem 만들
   - 주문 만들기: Order객체 만들기
   - orders db에 주문 저장
   - order_items db에 주문 상품 저장
   - cart_items db에 변경된 장바구니 업데이트




## 2. 주문 내역 전체 조회 API ( 자신이 주문한 내역 )
### Request
- method : GET
- url : /orders
- Authorization Header : 멤버 인증 정보 ( Basic )

### Response
- status code : ok ( 200 )
- Body

    ```json
    {
    	orders : [
    		{
    				order_id: 1,
    					items : [
    							{
    							    "id": 1,
    							    "quantity": 5,
    							    "product": {
    							        "id": 1,
    							        "price": 10000,
    							        "name": "치킨",
    							        "imageUrl": "http://example.com/chicken.jpg"
    							    }
    							},
    							...
    					],
    					product_price : 30000, //상품들의 총 가격
    					discount_price : 3000, //할인 가격
    					delivery_fee : 3000 //배달 가격
    					total_price : 30000 //총 가격
    		},
    		...
    	]
    }
    ```




## 3. 주문 내역 단일 조회 API ( 자신이 주문한 내역 )
### Request
- method : GET
- url : /orders/{orderId}
- Authorization Header : 멤버 인증 정보 ( Basic )

### Response
- status code : ok ( 200 )
- Body
    ```json
    
    {
    			order_id : 1,
    			items : [
    					{
    					    "id": 1,
    					    "quantity": 5,
    					    "product": {
    					        "id": 1,
    					        "price": 10000,
    					        "name": "치킨",
    					        "imageUrl": "http://example.com/chicken.jpg"
    					    }
    					},
    					...
    			],
    			product_price : 30000, //상품들의 총 가격
    			discount_price : 3000, //할인 가격
    			delivery_fee : 3000 //배달 가격
    			total_price : 30000 //총 가격
    }
    ```




## 4. 주문 내역 삭제 API
### Request
- method : DELETE
- url : /orders/{orderId}
- Authorization Header : 멤버 인증 정보 ( Basic )

### Response
- status code :  no_content ( 204 )


swap 메모리 할당하기
git actions ci.cd
genkins

