package cart.application.coupon.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.type.CouponInfo;

public class CouponDetailResponse {

	private Long id;
	private String name;
	private String couponType;
	private BigDecimal discount;
	private List<SerialNumberResponse> serialNumbers;

	public CouponDetailResponse() {

	}

	public CouponDetailResponse(final Long id, final String name, final String couponType, final BigDecimal discount,
		final List<SerialNumberResponse> serialNumbers) {
		this.id = id;
		this.name = name;
		this.couponType = couponType;
		this.discount = discount;
		this.serialNumbers = serialNumbers;
	}

	public static CouponDetailResponse from(final Coupon coupon) {
		final CouponInfo couponInfo = coupon.getCouponInfo();
		final List<SerialNumber> serialNumbers = coupon.getSerialNumbers();

		return new CouponDetailResponse(couponInfo.getId(), couponInfo.getName(), couponInfo.getCouponType().getType(),
			couponInfo.getDiscount(), convertToSerialNumberResponse(serialNumbers));
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

	public List<SerialNumberResponse> getSerialNumbers() {
		return serialNumbers;
	}

	private static List<SerialNumberResponse> convertToSerialNumberResponse(final List<SerialNumber> serialNumbers) {
		return serialNumbers.stream()
			.map(SerialNumberResponse::new)
			.collect(Collectors.toList());
	}
}
