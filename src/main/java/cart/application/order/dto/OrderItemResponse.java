package cart.application.order.dto;

import java.math.BigDecimal;

import cart.domain.order.OrderItem;
import cart.domain.product.Product;

public class OrderItemResponse {

	private Long id;
	private String name;
	private String imageUrl;
	private Integer quantity;
	private BigDecimal totalPrice;

	public OrderItemResponse() {
	}

	public OrderItemResponse(final Long id, final String name, final String imageUrl, final Integer quantity,
		final BigDecimal totalPrice) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderItemResponse from(final OrderItem orderItem) {
		final Product product = orderItem.getProduct();
		return new OrderItemResponse(orderItem.getId(), product.getName(), product.getImageUrl(),
			orderItem.getQuantity(), orderItem.calculatePrice());
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

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
}
