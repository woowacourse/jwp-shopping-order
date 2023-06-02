package cart.domain.product;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void 같은_id인지_확인한다(final Long productId, final boolean expected) {
        //given
        final Product product = new Product(1L, "chicken", 20000, "chicken.jpeg");

        //when
        final boolean actual = product.isSameId(productId);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
