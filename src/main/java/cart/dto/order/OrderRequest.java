package cart.dto.order;

import java.util.List;

public class OrderRequest {
    private Long totalPrice;
    private List<OrderItemDto> orderItemDtoList;
    private Long usedPoint;

    public OrderRequest() {
    }

    public OrderRequest(Long totalPrice, List<OrderItemDto> orderItemDtoList,Long usedPoint) {
        this.totalPrice = totalPrice;
        this.orderItemDtoList = orderItemDtoList;
        this.usedPoint = usedPoint;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItemDto> getOrderItemDtoList() {
        return orderItemDtoList;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }
}
