package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("물품 도메인 테스트")
class ProductTest {

    @DisplayName("물품을 생성합니다.")
    @CsvSource(value = {"0:0", "10:10", "1000:1000"}, delimiter = ':')
    @ParameterizedTest
    void createProduct(int price, int stock) {
        //when
        Product product = new Product(null, price, null, stock);

        // then
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @DisplayName("물품 재고를 음수로 할 수 없습니다.")
    @CsvSource(value = {"0:-1", "0:-10", "0:-1000"}, delimiter = ':')
    @ParameterizedTest
    void doseNotCreateStockUnderZero(int price, int stock) {
        //when, then
        assertThatThrownBy(() -> new Product(null, price, null, stock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageEndingWith("재고는 음수가 될 수 없습니다.");
    }

    @DisplayName("물품 가격을 음수로 할 수 없습니다.")
    @CsvSource(value = {"-1:0", "-10:10", "-1000:1000"}, delimiter = ':')
    @ParameterizedTest
    void doseNotCreateNegativePrice(int price, int stock) {
        //when, then
        assertThatThrownBy(() -> new Product(null, price, null, stock))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("가격은 음수가 될 수 없습니다.");
    }
}
