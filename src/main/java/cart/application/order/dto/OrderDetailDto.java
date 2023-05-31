package cart.application.order.dto;

import java.util.List;

public class OrderDetailDto {

	private final Long orderId;
	private final List<OrderItemDto> orderItemDtos;
	private final Long totalPrice;
	private final Long deliveryFee;

	public OrderDetailDto(final Long orderId, final List<OrderItemDto> orderItemDtos, final Long totalPrice,
		final Long deliveryFee) {
		this.orderId = orderId;
		this.orderItemDtos = orderItemDtos;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemDto> getOrderItemDtos() {
		return orderItemDtos;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}
}
