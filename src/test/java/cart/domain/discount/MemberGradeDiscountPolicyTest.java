package cart.domain.discount;

import cart.domain.MemberGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberGradeDiscountPolicyTest {

    @DisplayName("회원 등급별로 할인 금액을 구할 수 있다")
    @Test
    void calculateDiscountAmount() {
        //given
        final MemberGradeDiscountPolicy gold = new MemberGradeDiscountPolicy(MemberGrade.GOLD);
        final MemberGradeDiscountPolicy silver = new MemberGradeDiscountPolicy(MemberGrade.SILVER);
        final MemberGradeDiscountPolicy bronze = new MemberGradeDiscountPolicy(MemberGrade.BRONZE);
        final int price = 10000;

        //when
        final int goldDiscountAmount = gold.calculateDiscountAmount(price);
        final int silverDiscountAmount = silver.calculateDiscountAmount(price);
        final int bronzeDiscountAmount = bronze.calculateDiscountAmount(price);

        //then
        assertSoftly(soft -> {
            assertThat(goldDiscountAmount).isEqualTo(500);
            assertThat(silverDiscountAmount).isEqualTo(300);
            assertThat(bronzeDiscountAmount).isEqualTo(100);
        });
    }
}
