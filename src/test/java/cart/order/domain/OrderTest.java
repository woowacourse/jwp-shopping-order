package cart.order.domain;

import cart.exception.DiscordException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTest {
    private OrderInfo orderInfo1;
    private OrderInfo orderInfo2;
    private OrderInfo orderInfo3;
    
    @BeforeEach
    void setUp() {
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, false);
        orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        final Product product3 = new Product("귤", 30000L, "cc", 20.0, true);
        orderInfo3 = new OrderInfo(product3, "귤", 30000L, "cc", 30L);
    }
    
    @Test
    void 입력된_원가와_계산된_원가가_다르면_예외_처리() {
        // expect
        assertThatThrownBy(() -> new Order(List.of(orderInfo1, orderInfo2), 476000L, 600L, 47500L))
                .isInstanceOf(DiscordException.class);
    }
    
    @Test
    void 입력된_pointToAdd와_계산된_pointToAdd가_다르면_예외_처리() {
        // expect
        assertThatThrownBy(() -> new Order(List.of(orderInfo1, orderInfo2), 475000L, 600L, 47600L))
                .isInstanceOf(DiscordException.class);
    }
    
    @ParameterizedTest(name = "{displayName} : usedPoint = {0}")
    @ValueSource(longs = {-1L, 1275001L})
    void 입력된_usedPoint가_정상_범위를_벗어나면_예외_처리(final Long usedPoint) {
        // expect
        assertThatThrownBy(() -> new Order(List.of(orderInfo1, orderInfo2, orderInfo3), 1375000L, usedPoint, 227500L))
                .isInstanceOf(DiscordException.class);
    }
}
