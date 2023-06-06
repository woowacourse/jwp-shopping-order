package cart.application.coupon.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SerialNumberRequest {

	@NotNull
	@Positive(message = "추가 발행 매수는 1장 이상이어야합니다.")
	private Integer couponCount;

	public SerialNumberRequest() {
	}

	public SerialNumberRequest(final Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Integer getCouponCount() {
		return couponCount;
	}
}
