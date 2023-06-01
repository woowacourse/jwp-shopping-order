package cart.order.domain;

import cart.order.domain.OrderInfo;
import cart.product.domain.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class OrderInfoTest {
    @Test
    void 물품_가격과_수량에_따른_가격을_구한다() {
        // given
        final Product product = new Product("사과", 10000L, "aa", 10.0, true);
        final OrderInfo orderInfo = new OrderInfo(product, "사과", 10000L, "aa", 15L);

        // when
        final Long finalPrice = orderInfo.calculateProductPriceWithQuantity();
        
        // then
        assertThat(finalPrice).isEqualTo(150000);
    }
}
