package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountTest {

    @Test
    @DisplayName("기존 가격과 할인된 가격을 통해 discount 객체 생성을 테스트한다.")
    void of() {
        Discount discount = Discount.of(10000, 5000);

        assertThat(discount.getOriginPrice()).isEqualTo(10000);
        assertThat(discount.getDiscountPrice()).isEqualTo(-5000);
        assertThat(discount.getPriceAfterDiscount()).isEqualTo(5000);
    }

}