package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Amount;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductsTest {

    @Test
    @DisplayName("상품들의 총 금액을 구한다.")
    void testCalculateTotalAmount() {
        //given
        final Products products = new Products(List.of(
            new Product("name1", Amount.of(1_000), "url1"),
            new Product("name2", Amount.of(2_000), "url2")));

        //when
        final Amount amount = products.calculateTotalAmount();

        //then
        assertThat(amount).isEqualTo(Amount.of(3_000));
    }
}
