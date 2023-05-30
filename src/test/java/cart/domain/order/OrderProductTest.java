package cart.domain.order;

import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductTest {

    @DisplayName("가격과 수량의 곱인 총 가격을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"1_000:2:2_000", "500:5:2_500", "10_000:3:30_000"}, delimiter = ':')
    void getAmountTest(final int price, final int quantity, final int expectAmount) {
        final Product product = new Product("name", price, "imageUrl");
        final OrderProduct orderProduct = new OrderProduct(product, product.getPrice(), quantity);
        assertThat(orderProduct.getAmount()).isEqualTo(expectAmount);
    }
}
