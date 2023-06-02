package cart.order.domain;

import cart.exception.DiscordException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTest {
    @Test
    void 최종_주문_가격을_구한다() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, true);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        final Order order = new Order(member, List.of(orderInfo1, orderInfo2), 475000L, 600L, 47500L);
        
        // when
        final Long payment = order.order();
        
        // then
        assertThat(payment).isEqualTo(474400L);
    }
    
    @Test
    void 주문_후_적립금을_계산한다() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, true);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        final Order order = new Order(member, List.of(orderInfo1, orderInfo2), 475000L, 600L, 47500L);
        
        // when
        order.order();
        
        // then
        assertThat(order.getMember().getPoint()).isEqualTo(47900L);
    }
    
    @Test
    void 입력된_원가와_계산된_원가가_다르면_예외_처리() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, true);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        
        // expect
        assertThatThrownBy(() -> new Order(member, List.of(orderInfo1, orderInfo2), 476000L, 600L, 47500L))
                .isInstanceOf(DiscordException.class);
    }
    
    @Test
    void 입력된_pointToAdd와_계산된_pointToAdd가_다르면_예외_처리() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, true);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        
        // expect
        assertThatThrownBy(() -> new Order(member, List.of(orderInfo1, orderInfo2), 475000L, 600L, 47600L))
                .isInstanceOf(DiscordException.class);
    }
    
    @ParameterizedTest(name = "{displayName} : usedPoint = {0}")
    @ValueSource(longs = {-1L, 1275001L})
    void 입력된_usedPoint가_정상_범위를_벗어나면_예외_처리(final Long usedPoint) {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000L, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000L, "aa", 15L);
        final Product product2 = new Product("바나나", 10000L, "bb", 10.0, false);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000L, "bb", 10L);
        final Product product3 = new Product("귤", 30000L, "cc", 20.0, true);
        final OrderInfo orderInfo3 = new OrderInfo(product3, "귤", 30000L, "cc", 30L);
        
        // expect
        assertThatThrownBy(() -> new Order(member, List.of(orderInfo1, orderInfo2, orderInfo3), 1375000L, usedPoint, 227500L))
                .isInstanceOf(DiscordException.class);
    }
}
