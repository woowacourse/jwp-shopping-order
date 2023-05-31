package cart.fixture;

import cart.entity.OrderDetailEntity;

public class OrderDetailEntityFixture {

    public static final OrderDetailEntity ODO1 = OrderDetailEntity.builder()
            .ordersId(1L)
            .productId(1L)
            .productName("치킨")
            .productPrice(10_000)
            .productImageUrl("http://example.com/chicken.jpg")
            .orderQuantity(10)
            .build();
}
