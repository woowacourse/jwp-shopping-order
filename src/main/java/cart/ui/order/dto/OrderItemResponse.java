package cart.ui.order.dto;

import cart.application.order.dto.OrderItemDto;

public class OrderItemResponse {

	private Long id;
	private String name;
	private String imageUrl;
	private Integer quantity;
	private Long totalPrice;

	public OrderItemResponse() {
	}

	public OrderItemResponse(final OrderItemDto orderItemDto) {
		this(orderItemDto.getId(), orderItemDto.getName(), orderItemDto.getImageUrl(), orderItemDto.getQuantity(),
			orderItemDto.getTotalPrice());
	}

	public OrderItemResponse(final Long id, final String name, final String imageUrl, final Integer quantity,
		final Long totalPrice) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
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
