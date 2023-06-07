package cart.order.domain;

import cart.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class OrderInfosTest {

    private OrderInfos orderInfos;

    @BeforeEach
    void setUp() {
        final Product product = new Product("사과", 10000L, "aa", 10.0, true);
        final OrderInfo orderInfo = new OrderInfo(product, "사과", 10000L, "aa", 15L);
        orderInfos = new OrderInfos(List.of(orderInfo));
    }

    @Test
    void 모든_물품의_가격과_수량을_고려해서_계산한다() {
        // when
        final Long price = orderInfos.calculateAllProductPriceWithQuantity();

        // then
        assertThat(price).isEqualTo(150000L);
    }

    @Test
    void 최종_적립될_포인트를_계산한다() {
        // when
        final Long pointToAdd = orderInfos.calculatePointToAdd();

        // then
        assertThat(pointToAdd).isEqualTo(15000L);
    }

    @Test
    void 사용할_수_있는_최대_포인트를_계산한다() {
        // when
        final Long availablePoint = orderInfos.calculateAvailablePoint();

        // then
        assertThat(availablePoint).isEqualTo(150000L);
    }
}
