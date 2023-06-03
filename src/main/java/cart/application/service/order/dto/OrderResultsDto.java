package cart.application.service.order.dto;

import java.util.List;

public class OrderResultsDto {
    private final List<OrderResultDto> orders;

    public OrderResultsDto(List<OrderResultDto> orders) {
        this.orders = orders;
    }

    public List<OrderResultDto> getOrders() {
        return orders;
    }
}
