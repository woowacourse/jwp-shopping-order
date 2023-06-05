package cart.domain.cart;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.cart.ProductNotValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품명은_100자_이하가_아니라면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Product(1L, "가".repeat(101), "1.jpg", 0))
                .isInstanceOf(ProductNotValidException.class)
                .hasMessage("상품명의 길이는 100자 이하여야합니다.");
    }

    @Test
    void 상품_이미지_값이_공백이라면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Product(1L, "텀블러", "", 1000L))
                .isInstanceOf(ProductNotValidException.class)
                .hasMessage("이미지는 공백일 수 없습니다.");
    }

    @Test
    void 상품_가격은_0원_이상이_아니라면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Product(1L, "텀블러", "1.jpg", -1))
                .isInstanceOf(ProductNotValidException.class)
                .hasMessage("상품 가격은 0원 이상이여야 합니다.");
    }
}
