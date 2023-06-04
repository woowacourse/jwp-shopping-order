package cart.application.coupon.dto;

import java.math.BigDecimal;
import java.util.List;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.type.CouponInfo;

public class AdminCouponResponse {

	private Long id;
	private String name;
	private String couponType;
	private BigDecimal discount;
	private Integer couponCount;

	public AdminCouponResponse() {
	}

	public AdminCouponResponse(final Long id, final String name, final String couponType, final BigDecimal discount,
		final Integer couponCount) {
		this.id = id;
		this.name = name;
		this.couponType = couponType;
		this.discount = discount;
		this.couponCount = couponCount;
	}

	public static AdminCouponResponse from(final Coupon coupon) {
		final CouponInfo couponInfo = coupon.getCouponInfo();
		final List<SerialNumber> serialNumbers = coupon.getSerialNumbers();
		return new AdminCouponResponse(couponInfo.getId(), couponInfo.getName(), couponInfo.getCouponType().name(),
			couponInfo.getDiscount(),
			serialNumbers.size());
	}

	public Long getId() {
		return id;
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
