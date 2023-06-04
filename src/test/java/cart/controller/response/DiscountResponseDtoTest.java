package cart.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountResponseDtoTest {

    @Test
    @DisplayName("OriginPrice 와 Discount 가 적용된 가격을 주입하면 할인된 가격과 총 가격을 반환한다.")
    void of() {
        DiscountResponseDto discountResponseDto = DiscountResponseDto.of(10000, 1000);

        assertThat(discountResponseDto.getTotalPrice()).isEqualTo(1000);
        assertThat(discountResponseDto.getDiscountPrice()).isEqualTo(-9000);
    }

}