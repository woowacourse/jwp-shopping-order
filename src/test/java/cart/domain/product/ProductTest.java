package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품_이름이_없으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Product(null, null, new ImageUrl("www.test.com"), new Price(1000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 필수 입니다.");
    }

    @Test
    void 상품_이미지가_없으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Product(null, new Name("상품"), null, new Price(1000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이미지는 필수 입니다.");
    }

    @Test
    void 상품_가격이_없으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Product(null, new Name("상품"), new ImageUrl("www.test.com"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 필수 입니다.");
    }
}
