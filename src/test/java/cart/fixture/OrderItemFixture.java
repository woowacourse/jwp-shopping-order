package cart.fixture;

import cart.domain.VO.Money;
import cart.domain.order.OrderItem;
import cart.entity.OrderItemEntity;

@SuppressWarnings("NonAsciiCharacters")
public class OrderItemFixture {

    public static OrderItem 상품_8900원_1개_주문 = new OrderItem("pizza1", "pizza1.png", Money.from(8900L), 1);
    public static OrderItem 상품_18900원_1개_주문 = new OrderItem("pizza2", "pizza2.png", Money.from(18900L), 1);
    public static OrderItem 상품_28900원_1개_주문 = new OrderItem("pizza3", "pizza3.png", Money.from(28900L), 1);

    public static OrderItem 상품_8900원_주문(final int quantity) {
        return new OrderItem("pizza1", "pizza1.png", Money.from(8900L), quantity);
    }

    public static OrderItem 상품_18900원_주문(final int quantity) {
        return new OrderItem("pizza2", "pizza2.png", Money.from(18900L), quantity);
    }

    public static OrderItem 상품_28900원_주문(final int quantity) {
        return new OrderItem("pizza3", "pizza3.png", Money.from(28900L), quantity);
    }

    public static OrderItemEntity _주문_상품_아이템_엔티티_생성(final int price, final int quantity, final Long orderId) {
        return new OrderItemEntity("주문 상품", "pizza.png", price, quantity, orderId);
    }
}
