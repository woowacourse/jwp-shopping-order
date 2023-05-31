package cart.fixture;

import cart.entity.OrderItemEntity;

@SuppressWarnings("NonAsciiCharacters")
public class OrderItemFixture {

    public static OrderItemEntity _주문_상품_아이템_엔티티_생성(final int price, final int quantity, final Long orderId) {
        return new OrderItemEntity("주문 상품", "pizza.png", price, quantity, orderId);
    }
}
