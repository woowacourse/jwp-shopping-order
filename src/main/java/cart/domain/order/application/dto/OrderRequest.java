package cart.domain.order.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.order.domain.dto.OrderCartItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    public List<Long> getCartItemIds() {
        return orderCartItemDtos.stream()
                .map(OrderCartItemDto::getCartItemId)
                .collect(Collectors.toUnmodifiableList());
    }
}
