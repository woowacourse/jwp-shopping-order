package cart.dto.apidatamapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static cart.domain.discountpolicy.DiscountType.AMOUNT;
import static cart.domain.discountpolicy.DiscountType.RATE;
import static org.assertj.core.api.Assertions.assertThat;

class DiscountAmountMapperTest {

    @Nested
    @DisplayName("domain, dto간 정량할인 수치 변환 테스트")
    class AmountDiscountMapTest {

        @Test
        @DisplayName("도메인수치에서 api body 수치로 변환한다")
        void domain_to_api_body_test() {
            // given
            double domainValue = 1000.0;

            // when
            int apiBodyValue = DiscountAmountMapper.domainValueToApiBodyAmount(AMOUNT, domainValue);

            // then
            assertThat(apiBodyValue).isEqualTo(1000);
        }

        @Test
        @DisplayName("api body수치에서 도메인수치로 변환한다")
        void api_body_to_domain_test() {
            // given
            int apiBodyValue = 1000;

            // when
            double domainValue = DiscountAmountMapper.apiBodyAmountToDomainValue(AMOUNT, apiBodyValue);

            // then
            assertThat(domainValue).isEqualTo(1000.0);
        }
    }

    @Nested
    @DisplayName("domain, dto간 정률할인 수치 변환 테스트")
    class RateDiscountMapTest {

        @Test
        @DisplayName("도메인수치에서 api body 수치로 변환한다")
        void domain_to_api_body_test() {
            // given
            double domainValue = 0.9;

            // when
            int apiBodyValue = DiscountAmountMapper.domainValueToApiBodyAmount(RATE, domainValue);

            // then
            assertThat(apiBodyValue).isEqualTo(10);
        }

        @Test
        @DisplayName("api body수치에서 도메인수치로 변환한다")
        void api_body_to_domain_test() {
            // given
            int apiBodyValue = 10;

            // when
            double domainValue = DiscountAmountMapper.apiBodyAmountToDomainValue(RATE, apiBodyValue);

            // then
            assertThat(domainValue).isEqualTo(0.9);
        }
    }
}
