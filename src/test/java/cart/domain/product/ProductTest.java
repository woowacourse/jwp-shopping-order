package cart.domain.product;

import cart.exception.SalePercentageInvalidRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductTest {

    @DisplayName("상품에 세일을 적용한다.")
    @Test
    void apply_sale_on_product() {
        // given
        Product product = createProduct();

        // when
        product.applySale(30);

        // then
        assertAll(
                () -> assertThat(product.getAppliedDiscountOrOriginPrice()).isEqualTo(14000),
                () -> assertThat(product.getSalePrice()).isEqualTo(6000),
                () -> assertThat(product.isOnSale()).isEqualTo(true)
        );
    }

    @DisplayName("상품의 세일을 제거한다.")
    @Test
    void un_apply_sale_on_product() {
        // given
        Product product = createProduct();
        product.applySale(10);

        // when
        product.unApplySale();

        // then
        assertThat(product.isOnSale()).isEqualTo(false);
    }

    @DisplayName("상품의 세일 적용시 1~100% 까지 가능하다.")
    @ValueSource(ints = {-1, 0, 101})
    @ParameterizedTest
    void throws_exception_when_sale_value_invalid_range(final int value) {
        // given
        Product product = createProduct();

        // when & then
        assertThatThrownBy(() -> product.applySale(value))
                .isInstanceOf(SalePercentageInvalidRangeException.class);
    }

    @DisplayName("세일 적용이 되지 않고, 세일 적용 후 금액을 조회하면 원래 가격이 나온다.")
    @Test
    void returns_origin_price_when_get_product_un_applied_coupon() {
        // given
        Product product = createProduct();

        // when
        int result = product.getAppliedDiscountOrOriginPrice();

        // then
        assertThat(result).isEqualTo(product.getPrice());
    }

    @DisplayName("세일 중이 아니라면 세일되는 가격을 조회할 때 0원이 나온다.")
    @Test
    void returns_zero_won_when_get_price_of_un_applied_product() {
        // given
        Product product = createProduct();

        // when
        int result = product.getSalePrice();

        // then
        assertThat(result).isEqualTo(0);
    }
}
