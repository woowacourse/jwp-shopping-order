package cart.repository.mapper;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;

public class OrderProductMapper {

    public static OrderProduct toDomain(OrderProductEntity orderProductEntity) {
        return new OrderProduct(
                orderProductEntity.getId(),
                extractProduct(orderProductEntity),
                orderProductEntity.getQuantity()
        );
    }

    private static Product extractProduct(OrderProductEntity orderProductEntity) {
        return ProductMapper.toDomain(new ProductEntity(
                orderProductEntity.getProductId(),
                orderProductEntity.getProductName(),
                orderProductEntity.getProductPrice(),
                orderProductEntity.getProductImageUrl()
        ));
    }

    public static OrderProductEntity toEntity(Order order, OrderProduct orderProduct) {
        return new OrderProductEntity(
                orderProduct.getId(),
                order.getId(),
                new ProductEntity(orderProduct.getProductId(), null, null, null),
                orderProduct.getProduct().getName(),
                orderProduct.getProduct().getPrice(),
                orderProduct.getProduct().getImageUrl(),
                orderProduct.getQuantity()
        );
    }
}
