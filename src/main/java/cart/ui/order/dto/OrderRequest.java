package cart.ui.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {

	@JsonProperty("cartItemIdList")
	private List<Long> cartItemIds;
	private Long totalPrice;
	private Long deliveryFee;

	public OrderRequest() {
	}

	public OrderRequest(final List<Long> cartItemIds, final Long totalPrice, final Long deliveryFee) {
		this.cartItemIds = cartItemIds;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
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
