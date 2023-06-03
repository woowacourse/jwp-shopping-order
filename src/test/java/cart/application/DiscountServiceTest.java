package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dto.response.DiscountResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DiscountServiceTest {

    @Autowired
    private DiscountService discountService;

    @Test
    void 금액과_회원_등급을_받아_할인_정보를_반환한다() {
        // when
        final List<DiscountResponse> discountResponse = discountService.getDiscount(5000, "silver");

        // then
        assertAll(
                () -> assertThat(discountResponse).hasSize(2),
                () -> assertThat(discountResponse).extracting(
                        DiscountResponse::getPolicyName,
                        DiscountResponse::getDiscountRate,
                        DiscountResponse::getDiscountPrice
                ).contains(
                        tuple("memberGradeDiscount", 0.03, 150),
                        tuple("priceDiscount", 0.01, 50)
                )
        );
    }

}
