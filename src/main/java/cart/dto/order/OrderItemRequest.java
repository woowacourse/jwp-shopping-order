package cart.dto.order;

import java.util.List;

public class OrderItemRequest {

    private List<OrderItemDto> orderItemDtoList;

    public OrderItemRequest() {
    }

    public OrderItemRequest(final List<OrderItemDto> orderItemDtoList) {
        this.orderItemDtoList = orderItemDtoList;
    }

    public List<OrderItemDto> getOrderItemDtoList() {
        return orderItemDtoList;
    }
}
