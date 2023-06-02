# jwp-shopping-order

# 2단계 기능 목록

- [x] Basic 인증방식이 아닐 경우 예외를 처리한다.
- [x] 상품이 존재하지 않을 경우 예외를 처리한다.
- [x] 장바구니에 상품이 존재하지 않는 경우 예외를 처리한다.
- [x] 주문할 때 인증이 되어있지 않을 경우 예외를 처리한다.
- [x] 주문 내역을 조회할 때, 주문 내역 아이디가 존재하지 않을때 예외를 처리한다.
- [x] 주문에 대한 결제 기록이 존재하지 않을 경우 예외를 처리한다.
- [x] 장바구니에서 선택한 상품들의 예상 결제 금액을 확인할 수 있다.
    - [x] member 로그인이 되지 않았을때, 가격 조회 막기
- [x] 장바구니에 담은 상품을 주문할 수 있다.
    - [x] 최소 1개 이상의 장바구니 상품을 주문할 수 있다.
    - [x] 비어있을 경우 예외처리
    - [x] 주문을 완료한 장바구니 상품은 장바구니에서 삭제된다.
    - [x] 요청한 회원의 상품이 아닐 경우 예외처리한다.
- [ ] 할인 정책을 적용할 수 있다.
    - [x] 할인 정책은 기준 금액과 할인율을 가지고 있다.
        - [ ] 할인율은 100% 이하여야한다.
    - [x] 기준 금액 이상 주문 시 전체 금액에서 할인율만큼 할인된다.
    - [x] 현재 주문에 적용 가능한 할인 정책을 찾을 수 있다.
      ex) 5만원 이상 주문 시 전체 금액에서 10% 할인이 된다.
- [x] 사용자 별로 주문 목록을 확인할 수 있다.
    - [ ] 최신순으로 주문 목록을 확인할 수 있다.
    - [x] 주문별로 주문번호, 상품별 정보(이름, 수량, 수량이 반영된 가격, 이미지) 를 확인할 수 있다.
- [x] 특정 주문의 상세 정보를 확인할 수 있다.
    - [x] 주문 번호, 상품별 정보와 결제 금액 정보를 확인할 수 있다.
        - [x] 결제 금액 정보는 주문 금액, 할인 금액, 배송비 정보를 포함한다.

# 클래스 다이어그램

```mermaid
classDiagram

    class CartItem {
        Long id
        int quantity
        Product product
        Member member
    }
    class Product {
        Long id
        String name
        Money price
        String imageUrl
    }

    class OrderItem {
        Long id
        String name
        String imageUrl
        int quantity
        Money totalPrice
    }

    class Order {
        Long id
        Member member
        OrderItem[] orderItems
        LocalDateTime orderTime     <! 고려중>
    }
    class Payment
    class PaymentRecord {
        Order order
        Money originalTotalPrice
        Map[DiscountPolicy, Money] policyToDiscountAmounts
        Map[DeliveryPolicy, Money] policyToDeliveryFees
    }
    
    class DiscountPolicy {
        <<interface>>
        + Money calculateDiscountAmount(Order order)
    }
    class DefaultDiscountPolicy {
        String name
        Money threshold
        double discountRate
        + Money calculateDiscountAmount(Order order)
    }

    class DeliveryPolicy {
        <<interface>>
        + Money calculateDeliveryFee(Order order)
    }
    class DefaultDeliveryPolicy {
        String name
        + Money calculateDeliveryFee(Order order)
    }

    Product --o CartItem 
    OrderItem ..> CartItem
    OrderItem --o Order
    DiscountPolicy --o PaymentRecord
    DeliveryPolicy --o PaymentRecord
    DiscountPolicy ..> Order
    DeliveryPolicy ..> Order
     Order--o PaymentRecord
    DefaultDeliveryPolicy ..|> DeliveryPolicy
    DefaultDiscountPolicy ..|> DiscountPolicy

```

## 추후 고려사항

- 적용될 수 있는 할인 정책이 여러 개인 경우 할인율이 가장 높은 정책을 적용한다.
- 여러 배송 정책은 조건을 가지고있고, 주문이 조건을 만족하는 경우 해당 정책을 적용한다.