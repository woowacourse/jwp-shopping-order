package cart.ui.order.dto;

import java.util.List;

public class OrderResponse {

	private Long orderId;
	private List<OrderItemResponse> products;

	public OrderResponse() {
	}

	public OrderResponse(final Long orderId, final List<OrderItemResponse> products) {
		this.orderId = orderId;
		this.products = products;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemResponse> getProducts() {
		return products;
	}
}
