package cart.domain.order.application.dto;

import java.util.List;

import cart.domain.order.domain.dto.OrderCartItemDto;

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
