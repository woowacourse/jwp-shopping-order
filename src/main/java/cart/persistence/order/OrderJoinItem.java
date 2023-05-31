package cart.persistence.order;

import cart.domain.order.OrderItem;
import cart.domain.product.Product;

public class OrderJoinItem {

	private Long orderId;
	private Long deliveryFee;
	private OrderItem orderItem;

	public OrderJoinItem(final Long orderId, final Long deliveryFee, final Long orderItemId, final String itemName,
		final Long itemPrice, final String itemImageUrl, final int quantity) {
		this.orderId = orderId;
		this.deliveryFee = deliveryFee;
		this.orderItem = new OrderItem(orderItemId, new Product(itemName, itemPrice, itemImageUrl), quantity);
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}
}
