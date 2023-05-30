package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceServiceTest {

    @Autowired
    PriceService priceService;

    @Test
    @DisplayName("가격과 등급을 통해 적용된 모든 가격정책의 상세내용을 반환한다.")
    void getDiscountInformation() {
        //when
        final var result = priceService.getDiscountInformation(10000, "gold").getDiscountInfoResponses();

        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(1).getPolicyName()).isEqualTo("priceDiscount"),
                () -> assertThat(result.get(1).getDiscountRate()).isEqualTo(0.02),
                () -> assertThat(result.get(1).getDiscountPrice()).isEqualTo(200),
                () -> assertThat(result.get(0).getPolicyName()).isEqualTo("gradeDiscount"),
                () -> assertThat(result.get(0).getDiscountRate()).isEqualTo(0.05),
                () -> assertThat(result.get(0).getDiscountPrice()).isEqualTo(500)
        );
    }
}
