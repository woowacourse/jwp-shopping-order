package cart.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdvancedDeliveryPolicyTest {

    private DeliveryPolicy deliveryPolicy = new AdvancedDeliveryPolicy();

    @ParameterizedTest(name = "상품의 가격이 {0}원 일 때 배송료는 {1}원 이다.")
    @CsvSource(value = {"49999:3000", "50000:0"}, delimiter = ':')
    public void getDeliveryFee(final Long productPrice, final Long expectDeliveryFee) {
        //given
        //when
        final Long deliveryFee = deliveryPolicy.getDeliveryFee(productPrice);

        //then
        assertThat(deliveryFee).isEqualTo(expectDeliveryFee);
    }
}
