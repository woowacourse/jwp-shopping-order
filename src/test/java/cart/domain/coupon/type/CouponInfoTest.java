package cart.domain.coupon.type;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CouponInfoTest {

	@Nested
	class DetermineCouponInfo {
		@Test
		void NotUsedTest() {
			// given
			final CouponType notUsed = CouponType.NOT_USED;
			final CouponInfo couponInfo = CouponInfo.of(0L, null, null, null);

			// when
			final String couponType = couponInfo.getCouponType().getType();

			// then
			assertThat(couponType).isEqualTo(notUsed.getType());
		}

		@Test
		void PercentageTest() {
			// given
			final CouponType percentage = CouponType.PERCENTAGE;
			final CouponInfo couponInfo = CouponInfo.of(1L, percentage.name(), "여름 맞이 15% 할인",
				BigDecimal.valueOf(15));

			// when
			final String couponType = couponInfo.getCouponType().getType();

			// then
			assertThat(couponType).isEqualTo(percentage.getType());
		}

		@Test
		void FixedAmountTest() {
			// given
			final CouponType fixedAmount = CouponType.FIXED_AMOUNT;
			final CouponInfo couponInfo = CouponInfo.of(2L, fixedAmount.name(), "신규 가입 5000원 할인",
				BigDecimal.valueOf(5000));

			// when
			final String couponType = couponInfo.getCouponType().getType();

			// then
			assertThat(couponType).isEqualTo(fixedAmount.getType());
		}

	}

	@Nested
	class CalculatePayments {
		private final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(10_000);

		@Test
		void NotUsedTest() {
			// given
			final CouponInfo couponInfo = CouponInfo.of(0L, null, null, null);

			// when
			final BigDecimal actual = couponInfo.calculatePayments(TOTAL_PRICE);

			// then
			assertThat(actual).isEqualTo(TOTAL_PRICE);
		}

		@Test
		void PercentageTest() {
			// given
			final CouponType percentage = CouponType.PERCENTAGE;
			final BigDecimal discount = BigDecimal.valueOf(15);
			final CouponInfo couponInfo = CouponInfo.of(1L, percentage.name(), "여름 맞이 15% 할인",
				discount);

			// when
			final BigDecimal actual = couponInfo.calculatePayments(TOTAL_PRICE);
			final BigDecimal expected = TOTAL_PRICE.multiply(BigDecimal.ONE.subtract(
				discount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)));

			// then
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		void FixedAmountTest() {
			// given
			final CouponType fixedAmount = CouponType.FIXED_AMOUNT;
			final BigDecimal discount = BigDecimal.valueOf(5000);
			final CouponInfo couponInfo = CouponInfo.of(2L, fixedAmount.name(), "신규 가입 5000원 할인",
				discount);

			// when
			final BigDecimal actual = couponInfo.calculatePayments(TOTAL_PRICE);
			final BigDecimal expected = TOTAL_PRICE.subtract(discount);

			// then
			assertThat(actual).isEqualTo(expected);
		}
	}

}
