package cart.domain.coupon.type;

public enum CouponType {
	PERCENTAGE("퍼센트 할인"),
	FIXED_AMOUNT("금액 할인"),
	NOT_USED("미사용");

	private final String type;

	CouponType(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
