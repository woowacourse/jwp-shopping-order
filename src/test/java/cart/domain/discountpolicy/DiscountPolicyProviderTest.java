package cart.domain.discountpolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DiscountPolicyProviderTest {

    @Autowired
    private DiscountPolicyProvider discountPolicyProvider;

    @Autowired
    private RateDiscountPolicy rateDiscountPolicy;

    @Test
    @DisplayName("타입으로 할인 정책을 가져온다.")
    void get_by_type_test() {
        // given
        DiscountType type = DiscountType.AMOUNT;

        // when
        DiscountPolicy discountPolicy = discountPolicyProvider.getByType(type);

        // then
        assertThat(discountPolicy).isInstanceOf(AmountDiscountPolicy.class);
    }

    @Test
    @DisplayName("정책 객체로 정책 타입을 가져온다.")
    void get_discount_type_test() {
        // given

        // when
        DiscountType discountType = discountPolicyProvider.getDiscountType(rateDiscountPolicy);

        // then
        assertThat(discountType).isEqualTo(DiscountType.RATE);
    }
}
