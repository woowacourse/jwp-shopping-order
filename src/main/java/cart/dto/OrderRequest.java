package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<OrderCartItemDto> orderCartItemDtos;

    private OrderRequest() {
    }

    public OrderRequest(List<OrderCartItemDto> orderCartItemDtos) {
        this.orderCartItemDtos = orderCartItemDtos;
    }

    public List<OrderCartItemDto> getOrderCartItemDtos() {
        return orderCartItemDtos;
    }
}
