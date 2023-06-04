package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 모든_필드가_같으면_같은_객체이다() {
        // given
        Product product1 = new Product(1L, "name", 1000, "imageUrl", 10);
        Product product2 = new Product(1L, "name", 1000, "imageUrl", 10);

        // expect
        assertThat(product1).isEqualTo(product2);
    }

    @Test
    void 주문_수량에_맞게_남은_재고를_수정한다() {
        // given
        int initStock = 20;
        Product product = new Product(1L, "apple", 1000, "appleImage", initStock);
        int quantity = 10;

        // when
        Product result = product.updateStock(quantity);

        // then
        assertThat(result.getStock()).isEqualTo(initStock - quantity);
    }

    @Test
    void 남은_재고보다_주문_수량이_많으면_예외를_발생한다() {
        // given
        int initStock = 10;
        Product product = new Product(1L, "apple", 1000, "appleImage", initStock);
        int quantity = 20;

        // expect
        assertThatThrownBy(() -> product.updateStock(quantity))
                .isInstanceOf(InsufficientStockException.class);
    }
}
