package cart.order.domain;

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
        assertThat(finalPrice).isEqualTo(150000L);
    }

    @Test
    void 적립될_포인트를_계산한다() {
        // given
        final Product product = new Product("사과", 10000L, "aa", 10.0, true);
        final OrderInfo orderInfo = new OrderInfo(product, "사과", 10000L, "aa", 15L);

        // when
        final Long pointToAdd = orderInfo.calculatePointToAdd();

        // then
        assertThat(pointToAdd).isEqualTo(15000L);
    }

    @Test
    void 사용할_수_있는_최대_포인트를_계산한다() {
        // given
        final Product product = new Product("사과", 10000L, "aa", 10.0, true);
        final OrderInfo orderInfo = new OrderInfo(product, "사과", 10000L, "aa", 15L);

        // when
        final Long availablePoint = orderInfo.calculateAvailablePoint();

        // then
        assertThat(availablePoint).isEqualTo(150000L);
    }
}
