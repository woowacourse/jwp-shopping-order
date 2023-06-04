package cart.application.coupon.dto;

import java.math.BigDecimal;

public class CouponRequest {

	private String name;
	private String couponType;
	private BigDecimal discount;
	private Integer couponCount;

	public CouponRequest() {
	}

	public CouponRequest(final String name, final String couponType, final BigDecimal discount,
		final Integer couponCount) {
		this.name = name;
		this.couponType = couponType;
		this.discount = discount;
		this.couponCount = couponCount;
	}

	public String getName() {
		return name;
	}

	public String getCouponType() {
		return couponType;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public Integer getCouponCount() {
		return couponCount;
	}
}
