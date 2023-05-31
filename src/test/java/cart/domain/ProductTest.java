package cart.domain;

import cart.exception.ProductException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    private String 상품_이름 = "validName";
    private int 상품_가격 = 1_000;
    private String 상품_이미지_주소 = "https://imageUrl";

    @Nested
    class 상품_이름이 {

        @Test
        void 한_글자보다_작으면_예외를_던진다() {
            assertThatThrownBy(() -> new Product("", 상품_가격, 상품_이미지_주소))
                    .isInstanceOf(ProductException.InvalidNameLength.class)
                    .hasMessageContaining("상품 이름은 1자 이상 255자 이하여야합니다.");
        }

        @Test
        void 최대_입력_가능한_값보다_작으면_예외를_던진다() {
            assertThatThrownBy(() -> new Product("a".repeat(256), 상품_가격, 상품_이미지_주소))
                    .isInstanceOf(ProductException.InvalidNameLength.class)
                    .hasMessageContaining("상품 이름은 1자 이상 255자 이하여야합니다.");
        }
    }

    @Test
    void 이미지_url_형식이_잘못된_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Product(상품_이름, 상품_가격, ""))
                .isInstanceOf(ProductException.InvalidImageUrl.class)
                .hasMessageContaining("잘못된 이미지 url입니다.");
    }
}
