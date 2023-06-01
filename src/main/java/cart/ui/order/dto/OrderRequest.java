package cart.ui.order.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {

	@NotEmpty(message = "한 가지 이상의 상품을 주문하셔야합니다.")
	@JsonProperty("cartItemIdList")
	private List<Long> cartItemIds;
	@NotNull
	@PositiveOrZero(message = "가격은 0원 보다 낮을 수 없습니다. ")
	private Long totalPrice;
	@NotNull
	@PositiveOrZero(message = "배달비 0원 보다 낮을 수 없습니다. ")
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
