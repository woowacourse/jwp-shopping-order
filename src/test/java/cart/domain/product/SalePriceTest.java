package cart.domain.product;

import cart.exception.SalePercentageInvalidRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SalePriceTest {

    @DisplayName("salePrice가 null값이면, 기본 saleValue는 0원이다.")
    @Test
    void create_default_sale_when_sale_price_is_null() {
        // given
        Integer salePrice = null;

        // when
        SalePrice result = SalePrice.from(salePrice);

        // then
        assertThat(result.getSalePrice()).isEqualTo(0);
    }

    @DisplayName("salePrice가 null값이 아니면, 기본 saleValue는 들어온 값이다..")
    @Test
    void create_sale_when_sale_price_is_not_null() {
        // given
        Integer salePrice = 10;

        // when
        SalePrice result = SalePrice.from(salePrice);

        // then
        assertThat(result.getSalePrice()).isEqualTo(10);
    }

    @DisplayName("세일의 범위가 1~100 사이의 수가 아니라면 예외를 던진다.")
    @ValueSource(ints = {-1, 0, 101})
    @ParameterizedTest
    void throws_exception_when_invalid_sale_range(final int percentage) {
        // given
        SalePrice salePrice = SalePrice.from(0);

        // when & then
        assertThatThrownBy(() -> salePrice.applySale(100, percentage))
                .isInstanceOf(SalePercentageInvalidRangeException.class);
    }

    @DisplayName("세일을 적용한다.")
    @Test
    void apply_sale_success() {
        // given
        SalePrice salePrice = SalePrice.createDefault();

        // when
        int price = salePrice.applySale(1000, 10);

        // then
        assertThat(price).isEqualTo(100);
    }

    @DisplayName("세일을 제거한다.")
    @Test
    void un_apply_sale_success() {
        // given
        SalePrice salePrice = SalePrice.createDefault();

        // when
        salePrice.applySale(1000, 10);
        salePrice.unApplySale();

        // then
        assertThat(salePrice.getSalePrice()).isEqualTo(0);
    }
}
