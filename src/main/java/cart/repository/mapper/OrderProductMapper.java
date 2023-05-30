package cart.repository.mapper;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;

public class OrderProductMapper {

    public static OrderProduct toDomain(OrderProductEntity orderProductEntity) {
        ProductEntity productEntity = new ProductEntity(
                orderProductEntity.getProductId(),
                orderProductEntity.getProductName(),
                orderProductEntity.getProductPrice(),
                orderProductEntity.getProductImageUrl()
        );
        return new OrderProduct(
                orderProductEntity.getId(),
                ProductMapper.toDomain(productEntity),
                orderProductEntity.getQuantity()
        );
    }

    public static OrderProductEntity toEntity(Order order, OrderProduct orderProduct) {
        return new OrderProductEntity(
                orderProduct.getId(),
                order.getId(),
                null,
                orderProduct.getProduct().getName(),
                orderProduct.getProduct().getPrice(),
                orderProduct.getProduct().getImageUrl(),
                orderProduct.getQuantity()
        );
    }
}
