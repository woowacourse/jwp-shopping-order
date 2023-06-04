package cart.domain.coupon;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import cart.error.exception.BadRequestException;

class SerialNumberTest {

	@Nested
	class generateSerialNumbersTest {
		@Test
		void normalCaseTest() {
			// given
			final int couponCount = 10;

			// when
			final List<SerialNumber> serialNumbers = SerialNumber.generateSerialNumbers(couponCount);

			// then
			assertThat(serialNumbers).hasSize(couponCount);
		}

		@Test
		void abnormalCaseTest() {
			// given
			final int couponCount = -1;

			// when & then
			assertThatThrownBy(() -> SerialNumber.generateSerialNumbers(couponCount))
				.isInstanceOf(BadRequestException.SerialNumber.class)
				.hasMessage("발행 매수는 음수가 될 수 없습니다.");
		}

	}
}
