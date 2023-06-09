package cart.domain.coupon;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import cart.domain.coupon.type.CouponInfo;
import cart.domain.coupon.type.CouponType;

class CouponTest {
	private CouponInfo couponInfo;

	@BeforeEach
	void setCouponInfo() {
		couponInfo = CouponInfo.of(1L, CouponType.PERCENTAGE.getType(), "a", BigDecimal.TEN);
	}

	@Nested
	@DisplayName("모든 시리얼 넘버가")
	class findUnissuedSerialNumberTest {

		@Test
		@DisplayName("사용되지는 않은 경우, 시리얼 번호를 찾아준다.")
		void returnOptionalPresent() {
			// given
			final Coupon coupon = new Coupon(couponInfo, List.of(
				new SerialNumber(null, "a", false),
				new SerialNumber(null, "b", false),
				new SerialNumber(null, "c", false)
			));

			// when
			final Optional<SerialNumber> optionalSerialNumber = coupon.findUnissuedSerialNumber();

			// then
			assertThat(optionalSerialNumber).isPresent();
		}

		@Test
		@DisplayName("사용된 경우, 시리얼번호를 반환하지 않는다.")
		void returnOptionalNull() {
			// given
			final Coupon coupon = new Coupon(couponInfo, List.of(
				new SerialNumber(null, "a", true)
			));

			// when
			final Optional<SerialNumber> optionalSerialNumber = coupon.findUnissuedSerialNumber();

			// then
			assertThat(optionalSerialNumber).isNotPresent();
		}

	}
}
