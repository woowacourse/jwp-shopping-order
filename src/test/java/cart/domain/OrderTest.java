package cart.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTest {
    @Test
    void 최종_주문_가격을_구한다() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000, "aa", 15);
        final Product product2 = new Product("바나나", 10000, "bb", 10.0, false);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000, "bb", 10);
        final Order order = new Order(member, List.of(orderInfo1, orderInfo2), 475000L, 600L, 47500L);
        
        // when
        final Long payment = order.calculatePayment();
        
        // then
        assertThat(payment).isEqualTo(474400L);
    }
    
    @Test
    void 입력된_원가와_계산된_원가가_다르면_예외_처리() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000, "aa", 15);
        final Product product2 = new Product("바나나", 10000, "bb", 10.0, false);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000, "bb", 10);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(member, List.of(orderInfo1, orderInfo2), 476000L, 600L, 47500L));
    }
    
    @Test
    void 입력된_pointToAdd와_계산된_pointToAdd가_다르면_예외_처리() {
        // given
        final Member member = new Member(null, "아벨", "1234", 1000L);
        final Product product1 = new Product("사과", 25000, "aa", 10.0, true);
        final OrderInfo orderInfo1 = new OrderInfo(product1, "사과", 25000, "aa", 15);
        final Product product2 = new Product("바나나", 10000, "bb", 10.0, false);
        final OrderInfo orderInfo2 = new OrderInfo(product2, "사과", 10000, "bb", 10);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(member, List.of(orderInfo1, orderInfo2), 475000L, 600L, 47600L));
    }
}
