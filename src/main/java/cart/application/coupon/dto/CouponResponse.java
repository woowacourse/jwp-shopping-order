package cart.application.coupon.dto;

import java.math.BigDecimal;

import cart.domain.coupon.type.CouponInfo;

public class CouponResponse {

	private Long id;
	private String name;
	private BigDecimal priceDiscount;

	public CouponResponse() {
	}

	public CouponResponse(final CouponInfo couponInfo) {
		this(couponInfo.getId(), couponInfo.getName(), couponInfo.getDiscount());
	}

	public CouponResponse(final Long id, final String name, final BigDecimal priceDiscount) {
		this.id = id;
		this.name = name;
		this.priceDiscount = priceDiscount;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPriceDiscount() {
		return priceDiscount;
	}
}
