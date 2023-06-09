package cart.fixture;

import cart.domain.Product;
import cart.entity.OrderItemEntity;

public class OrderItemFixture {

    public static OrderItemEntity getOrderItemEntity(Long id, Long orderId, Integer quantity, Product product) {
        return new OrderItemEntity(id, orderId, product.getId(), quantity, product.getName(), product.getPrice(),
                product.getImageUrl(), product.getPrice() * quantity);
    }
}
