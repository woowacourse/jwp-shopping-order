package cart.ui.order.dto;

import java.util.List;

public class OrderDetailResponse {

	private Long orderId;
	private List<OrderItemResponse> products;
	private Long totalPrice;
	private Long deliveryFee;

	public OrderDetailResponse() {
	}

	public OrderDetailResponse(final Long orderId, final List<OrderItemResponse> products, final Long totalPrice,
		final Long deliveryFee) {
		this.orderId = orderId;
		this.products = products;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemResponse> getProducts() {
		return products;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}
}
