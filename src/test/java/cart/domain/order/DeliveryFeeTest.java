package cart.domain.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import cart.error.exception.OrderException;

class DeliveryFeeTest {

	@Test
	@DisplayName("생성자에 값을 입력하지 않으면 3000을 반환한다.")
	void defaultConstructorTest() {
		//when
		final DeliveryFee deliveryFee = new DeliveryFee();

		//then
		assertThat(deliveryFee.getDeliveryFee()).isEqualTo(3000);
	}

	@Nested
	class ParametrizedConstructorTest {

		@ParameterizedTest
		@ValueSource(longs = {0L, 3000L})
		@DisplayName("0 이상의 값을 넣으면 객체가 생성된다.")
		void givenPositiveOrZero_thenReturn(final Long input) {
			//when
			final DeliveryFee deliveryFee = new DeliveryFee(input);

			//then
			assertThat(deliveryFee.getDeliveryFee()).isEqualTo(input);
		}

		@ParameterizedTest
		@ValueSource(longs = {-1L, -3000L})
		void givenNegative_thenThrowException(final Long input) {
			assertThatThrownBy(() -> new DeliveryFee(input))
				.isInstanceOf(OrderException.BadRequest.class)
				.hasMessage("배송비는 0원 미만이 될 수 없습니다.");
		}
	}

}
