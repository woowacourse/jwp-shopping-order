package cart.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {
    @ParameterizedTest(name = "{displayName} : pointToAdd = {0}, pointAvailable = {1}")
    @CsvSource(value = {"1000,true", "0,false"})
    void 포인트가_적립될_상품이면_적립금을_계산한다(final int pointToAdd, final boolean pointAvailable) {
        // given
        final Product product = new Product("사과", 10000, "aa", 10.0, pointAvailable);
        
        // when
        final int actualPointToAdd = product.calculatePointToAdd();
        
        // then
        assertThat(actualPointToAdd).isEqualTo(pointToAdd);
    }
}
