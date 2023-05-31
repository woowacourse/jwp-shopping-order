package cart.application.order.dto;

import java.util.List;

import cart.ui.order.dto.OrderRequest;

public class OrderAddDto {

	private final Long memberId;
	private final List<Long> cartItemIds;
	private final Long totalPrice;
	private final Long deliveryFee;

	public OrderAddDto(final Long memberId, OrderRequest request) {
		this(memberId, request.getCartItemIds(), request.getTotalPrice(), request.getDeliveryFee());
	}

	public OrderAddDto(final Long memberId, final List<Long> cartItemIds, final Long totalPrice,
		final Long deliveryFee) {
		this.memberId = memberId;
		this.cartItemIds = cartItemIds;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
	}

	public Long getMemberId() {
		return memberId;
	}

	public List<Long> getCartItemIds() {
		return cartItemIds;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}
}
