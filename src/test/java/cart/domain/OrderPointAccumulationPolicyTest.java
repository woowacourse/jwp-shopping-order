package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPointAccumulationPolicyTest {

    @DisplayName("주문 포인트 적립 금액을 구할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"49999:2500", "50000:4000", "99999:8000", "100000:10000"}, delimiter = ':')
    void calculateAccumulationPoint(int totalCost, int expected) {
        OrderPointAccumulationPolicy orderPointAccumulationPolicy = new OrderPointAccumulationPolicy(new OrderPointExpirePolicy());
        assertThat(orderPointAccumulationPolicy.calculateAccumulationPoint(totalCost).getValue()).isEqualTo(expected);
    }
}
