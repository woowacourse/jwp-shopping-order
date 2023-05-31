package cart.domain.order;

import java.util.List;

public class Order {

	private final Long id;
	private final List<OrderItem> orderItems;
	private final DeliveryFee deliveryFee;

	public Order(final Long id, final List<OrderItem> orderItems, final DeliveryFee deliveryFee) {
		this.id = id;
		this.orderItems = orderItems;
		this.deliveryFee = deliveryFee;
	}

	public boolean isSameTotalPrice(final Long totalPrice) {
		return calculateTotalProductPrice().equals(totalPrice);
	}

	public boolean isDifferentTotalPrice(final Long totalPrice) {
		return !isSameTotalPrice(totalPrice);
	}

	public Long calculateTotalProductPrice() {
		return orderItems.stream()
			.mapToLong(OrderItem::calculatePrice)
			.sum();
	}

	public Long calculateTotalOrderPrice() {
		return calculateTotalProductPrice() + deliveryFee.getDeliveryFee();
	}

	public Long getId() {
		return id;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public DeliveryFee getDeliveryFee() {
		return deliveryFee;
	}
}
