package cart.application.order.dto;

import java.util.List;

public class OrderDto {

	private final Long orderId;
	private final List<OrderItemDto> orderItemDtos;

	public OrderDto(final Long orderId, final List<OrderItemDto> orderItemDtos) {
		this.orderId = orderId;
		this.orderItemDtos = orderItemDtos;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemDto> getOrderItemDtos() {
		return orderItemDtos;
	}
}
