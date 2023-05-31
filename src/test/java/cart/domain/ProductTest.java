package cart.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {
    @Test
    void 포인트가_적립될_상품이면_적립금을_계산한다() {
        // given
        final Product product = new Product("사과", 10000, "aa", 10.0, true);
        
        // when
        final long actualPointToAdd = product.calculatePointToAdd();
        
        // then
        assertThat(actualPointToAdd).isEqualTo(1000);
    }
}
