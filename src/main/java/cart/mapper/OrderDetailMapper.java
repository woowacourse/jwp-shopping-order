package cart.mapper;

import cart.domain.OrderDetail;
import cart.dto.response.OrderProductResponse;
import cart.entity.OrderDetailEntity;
import cart.exception.OrderException;

public class OrderDetailMapper {

    private OrderDetailMapper() {
    }

    public static OrderDetail toOrderDetail(OrderDetailEntity entity) {
        try {
            return new OrderDetail(entity.getId(), entity.getProductId(), entity.getProductName(), entity.getProductImage(), entity.getProductQuantity(), entity.getProductPrice());
        } catch (Exception e) {
            throw new OrderException.ParseFail(entity);
        }
    }

    public static OrderProductResponse toResponse(OrderDetail detail) {
        try {
            return new OrderProductResponse(detail.getProductId(), detail.getProductName(), detail.getProductImage(), detail.getProductQuantity(), detail.getProductPrice(), detail.getProductPrice() * detail.getProductQuantity());
        } catch (Exception e) {
            throw new OrderException.ParseFail(detail);
        }
    }
}
