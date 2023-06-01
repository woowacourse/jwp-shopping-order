package cart.dto;

import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static List<OrderItemResponse> convertToOrderItemResponses(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItemResponse(
                        orderItemEntity.getProductId(),
                        orderItemEntity.getProductName(),
                        orderItemEntity.getProductPrice(),
                        orderItemEntity.getProductQuantity(),
                        orderItemEntity.getProductImageUrl()
                )).collect(Collectors.toList());
    }

    public static OrderResponse convertToOrderResponse(final OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        List<OrderItemResponse> orderItemResponses = convertToOrderItemResponses(orderItemEntities);

        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getSavingRate(),
                orderEntity.getPoints(),
                orderItemResponses
        );
    }
}
