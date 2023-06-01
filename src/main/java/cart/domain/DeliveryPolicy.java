package cart.domain;

public interface DeliveryPolicy {
    Money calculateDeliveryFee(Order order);

    // TODO: 주문을 받아서 적용 가능 여부 반환 기능
}
