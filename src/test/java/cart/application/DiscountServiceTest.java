package cart.application;

import cart.domain.MemberGrade;
import cart.domain.discount.MemberGradeDiscountPolicy;
import cart.domain.discount.PriceDiscountPolicy;
import cart.dto.DiscountInformationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @CsvSource(value = {"GOLD,10000,9400", "SILVER,10000,9600", "BRONZE,10000,9800"})
    @ParameterizedTest(name = "{0} 등급일 때 금액이 {1}원이면 할인된 금액은 {2}원이다.")
    void calculate(MemberGrade grade, int totalPrice, int expected) {
        final int price = discountService.calculateTotalPrice(grade, totalPrice);

        assertThat(price).isEqualTo(expected);
    }

    @DisplayName("현재 금액에 적용된 모든 할인 정책을 얻을 수 있다")
    @Test
    void getDiscountInfo() {
        //given
        final MemberGrade grade = MemberGrade.GOLD;
        final int totalPrice = 10000;

        //when
        final List<DiscountInformationResponse> info = discountService.getDiscountInfo(grade, totalPrice);

        //then
        assertSoftly(soft -> {
            assertThat(info).hasSize(2);
            assertThat(info)
                    .map(DiscountInformationResponse::getPolicyName)
                    .containsExactlyElementsOf(List.of(
                            new MemberGradeDiscountPolicy(MemberGrade.GOLD).getName(),
                            new PriceDiscountPolicy().getName()));
        });
    }
}
