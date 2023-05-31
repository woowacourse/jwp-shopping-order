package cart.application.order.dto;

import cart.domain.order.OrderItem;
import cart.domain.product.Product;

public class OrderItemDto {

	private final Long id;
	private final String name;
	private final String imageUrl;
	private final Integer quantity;
	private final Long totalPrice;

	public OrderItemDto(final Long id, final String name, final String imageUrl, final Integer quantity,
		final Long totalPrice) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderItemDto from(final OrderItem orderItem) {
		final Product product = orderItem.getProduct();
		return new OrderItemDto(orderItem.getId(), product.getName(), product.getImageUrl(), orderItem.getQuantity(),
			orderItem.calculatePrice());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}
}
