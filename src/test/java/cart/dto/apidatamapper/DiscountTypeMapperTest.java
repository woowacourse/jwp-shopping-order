package cart.dto.apidatamapper;

import cart.domain.discountpolicy.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static cart.domain.discountpolicy.DiscountType.AMOUNT;
import static cart.domain.discountpolicy.DiscountType.RATE;
import static org.assertj.core.api.Assertions.assertThat;

class DiscountTypeMapperTest {

    @Nested
    @DisplayName("domain, dto간 정량할인 타입 변환 테스트")
    class AmountDiscountMapTest {

        @Test
        @DisplayName("도메인타입에서 api body 타입으로 변환한다")
        void domain_to_api_body_test() {
            // given

            // when
            String apiBodyTypeValue = DiscountTypeMapper.domainToApiBodyString(AMOUNT);

            // then
            assertThat(apiBodyTypeValue).isEqualTo("amount");
        }

        @Test
        @DisplayName("api body타입에서 도메인타입으로 변환한다")
        void api_body_to_domain_test() {
            // given
            String apiBodyTypeValue = "amount";

            // when
            DiscountType domainType = DiscountTypeMapper.apiBodyStringToDomain(apiBodyTypeValue);

            // then
            assertThat(domainType).isEqualTo(AMOUNT);
        }
    }

    @Nested
    @DisplayName("domain, dto간 정률할인 타입 변환 테스트")
    class RateDiscountMapTest {

        @Test
        @DisplayName("도메인타입에서 api body 타입으로 변환한다")
        void domain_to_api_body_test() {
            // given

            // when
            String apiBodyTypeValue = DiscountTypeMapper.domainToApiBodyString(RATE);

            // then
            assertThat(apiBodyTypeValue).isEqualTo("percent");

        }

        @Test
        @DisplayName("api body타입에서 도메인타입으로 변환한다")
        void api_body_to_domain_test() {
            // given
            String apiBodyTypeValue = "percent";

            // when
            DiscountType domainType = DiscountTypeMapper.apiBodyStringToDomain(apiBodyTypeValue);

            // then
            assertThat(domainType).isEqualTo(RATE);
        }
    }
}
