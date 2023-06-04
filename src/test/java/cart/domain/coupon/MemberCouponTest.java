package cart.domain.coupon;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cart.domain.coupon.type.CouponInfo;
import cart.domain.coupon.type.FixedAmount;
import cart.domain.coupon.type.Percentage;
import cart.domain.monetary.Discount;

class MemberCouponTest {

	private List<CouponInfo> couponInfos;

	@BeforeEach
	public void setCouponInfos() {
		couponInfos = List.of(
			new FixedAmount(1L, "신규 가입 5,000원 할인", new Discount(BigDecimal.valueOf(5000))),
			new Percentage(2L, "여름맞이 10% 할인", new Discount(BigDecimal.valueOf(10))),
			new FixedAmount(3L, "배송료 3,000원 할인", new Discount(BigDecimal.valueOf(3000))),
			new Percentage(4L, "6월 기녕 6% 할인", new Discount(BigDecimal.valueOf(6)))
		);
	}

	@Test
	void contain() {
		// given
		final MemberCoupon memberCoupon = new MemberCoupon(1L, this.couponInfos);

		// when
		final boolean actual = memberCoupon.contain(3L);

		// then
		assertThat(actual).isTrue();
	}

	@Test
	void getCouponInfo() {
		// given
		final MemberCoupon memberCoupon = new MemberCoupon(1L, this.couponInfos);

		// when
		final CouponInfo actual = memberCoupon.getCouponInfo(1L).get();

		// then
		assertThat(actual).isEqualTo(this.couponInfos.get(0));
	}

	@Test
	void getCouponInfos() {
		// given
		final MemberCoupon memberCoupon = new MemberCoupon(1L, this.couponInfos);

		// when
		final List<CouponInfo> actual = memberCoupon.getCouponInfos();

		// then
		assertThat(actual).isEqualTo(couponInfos);
	}
}
