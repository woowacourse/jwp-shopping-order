package cart.dto.order;

import java.util.List;

public class OrderRequest {
    private Long totalPrice;
    private List<OrderItemDto> orderItemDtoList;

    public OrderRequest() {
    }

    public OrderRequest(Long totalPrice, List<OrderItemDto> orderItemDtoList) {
        this.totalPrice = totalPrice;
        this.orderItemDtoList = orderItemDtoList;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItemDto> getOrderItemDtoList() {
        return orderItemDtoList;
    }
}
