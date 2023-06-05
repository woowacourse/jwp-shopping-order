package cart.application.coupon.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.type.CouponInfo;

public class AdminCouponResponse {

	private Long id;
	private String name;
	private String couponType;
	private BigDecimal discount;
	private Integer leftOverCoupon;
	private Integer couponCount;

	public AdminCouponResponse() {
	}

	public AdminCouponResponse(final Long id, final String name, final String couponType, final BigDecimal discount,
		final Integer leftOverCoupon, final Integer couponCount) {
		this.id = id;
		this.name = name;
		this.couponType = couponType;
		this.discount = discount;
		this.leftOverCoupon = leftOverCoupon;
		this.couponCount = couponCount;
	}

	public static AdminCouponResponse from(final Coupon coupon) {
		final CouponInfo couponInfo = coupon.getCouponInfo();
		final List<SerialNumber> serialNumbers = coupon.getSerialNumbers();
		final int leftOverCoupon = getLeftOverCoupon(serialNumbers);
		return new AdminCouponResponse(couponInfo.getId(), couponInfo.getName(), couponInfo.getCouponType().name(),
			couponInfo.getDiscount(), leftOverCoupon, serialNumbers.size());
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

	public Integer getLeftOverCoupon() {
		return leftOverCoupon;
	}

	private static int getLeftOverCoupon(final List<SerialNumber> serialNumbers) {
		return (int)serialNumbers.stream()
			.filter(Predicate.not(SerialNumber::isIssued))
			.count();
	}
}
