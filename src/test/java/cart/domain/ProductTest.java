package cart.domain;

import cart.exception.ProductException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private Long validId = 1L;
    private String validName = "validName";
    private int validPrice = 1_000;
    private String validImageUrl = "imageUrl";

    @Test
    void 아이디를_입력하지_않으면_예외를_던진다() {
        assertThatThrownBy(() -> new Product(null, validName, validPrice, validImageUrl))
                .isInstanceOf(ProductException.InvalidIdByNull.class)
                .hasMessageContaining("상품 아이디를 입력해야 합니다.");
    }

    @Nested
    class 상품_이름이 {

        @Test
        void 한_글자보다_작으면_예외를_던진다() {
            assertThatThrownBy(() -> new Product(validId, "", validPrice, validImageUrl))
                    .isInstanceOf(ProductException.InvalidNameLength.class)
                    .hasMessageContaining("상품 이름은 1자 이상 255자 이하여야합니다.");
        }

        @Test
        void 최대_입력_가능한_값보다_작으면_예외를_던진다() {
            assertThatThrownBy(() -> new Product(validId, "a".repeat(256), validPrice, validImageUrl))
                    .isInstanceOf(ProductException.InvalidNameLength.class)
                    .hasMessageContaining("상품 이름은 1자 이상 255자 이하여야합니다.");
        }
    }

    @Test
    void 상품_가격이_1원보다_작으면_예외를_던진다() {
        assertThatThrownBy(() -> new Product(validId, validName, 0, validImageUrl))
                .isInstanceOf(ProductException.InvalidPrice.class)
                .hasMessageContaining("상품 가격은 1원 이상이어야 합니다.");
    }

    @Test
    void 이미지_url_형식이_잘못된_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Product(validId, validName, validPrice, ""))
                .isInstanceOf(ProductException.InvalidImageUrl.class)
                .hasMessageContaining("잘못된 이미지 url입니다.");
    }
}
