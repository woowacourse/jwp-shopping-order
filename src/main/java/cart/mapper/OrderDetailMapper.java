package cart.mapper;

import cart.domain.OrderDetail;
import cart.dto.OrderProductResponse;
import cart.entity.OrderDetailEntity;

public class OrderDetailMapper {

    private OrderDetailMapper() {
    }

    public static OrderDetail toOrderDetail(OrderDetailEntity entity) {
        if (entity == null) {
            return null;
        }
        return new OrderDetail(entity.getId(), entity.getProductId(), entity.getProductName(), entity.getProductImage(), entity.getProductQuantity(), entity.getProductPrice());
    }

    public static OrderProductResponse toResponse(OrderDetail detail) {
        if (detail == null) {
            return null;
        }

        return new OrderProductResponse(detail.getProductId(), detail.getProductName(), detail.getProductImage(), detail.getProductQuantity(), detail.getProductPrice(), detail.getProductPrice() * detail.getProductQuantity());
    }
}
