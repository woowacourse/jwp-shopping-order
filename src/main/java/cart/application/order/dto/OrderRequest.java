package cart.application.order.dto;

import java.math.BigDecimal;
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
	private BigDecimal totalPrice;
	@NotNull
	@PositiveOrZero(message = "배달비 0원 보다 낮을 수 없습니다. ")
	private BigDecimal deliveryFee;
	private Long couponId;

	public OrderRequest() {
	}

	public OrderRequest(final List<Long> cartItemIds, final BigDecimal totalPrice, final BigDecimal deliveryFee,
		final Long couponId) {
		this.cartItemIds = cartItemIds;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
		this.couponId = couponId;
	}

	public List<Long> getCartItemIds() {
		return cartItemIds;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public Long getCouponId() {
		return couponId;
	}
}
