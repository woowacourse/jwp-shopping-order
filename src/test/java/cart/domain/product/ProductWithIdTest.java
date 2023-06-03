package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductWithIdTest {

    @ParameterizedTest(name = "상품이 제거된 상품이면 true, 아니면 false를 반환한다.")
    @CsvSource(value = {"true:true", "false:false"}, delimiter = ':')
    void isDeleted(final boolean isDeleted, final boolean expected) {
        // given
        final ProductWithId productWithId = new ProductWithId(1L,
            new Product("치킨", 20000, "chicken_image_url", isDeleted));

        // when
        final boolean result = productWithId.isDeleted();

        // then
        assertThat(result)
            .isSameAs(expected);
    }
}
