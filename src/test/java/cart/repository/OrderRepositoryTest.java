package cart.repository;

import static cart.fixture.Fixture.A_CART_ITEM_CHICKEN;
import static cart.fixture.Fixture.A_CART_ITEM_SALAD;
import static cart.fixture.Fixture.A_ORDER;
import static cart.fixture.Fixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문할_수_있다() {
        // when
        final Order order = orderRepository.save(A_ORDER);

        // then
        assertThat(order.getId()).isNotNull();
    }

    @Test
    void id_와_이름으로_주문을_찾을_수_있다() {
        // given
        final Long id = orderRepository.save(A_ORDER).getId();

        // when
        final Order order = orderRepository.findById(id, MEMBER_A);

        // then
        assertAll(
                () -> assertThat(order.getMember()).usingRecursiveComparison().isEqualTo(MEMBER_A),
                () -> assertThat(order.getPrice()).isEqualTo(106_000),
                () -> assertThat(order.getCartItems()).hasSize(2)
        );
    }

    @Test
    void 사용자의_모든_주문을_찾을_수_있다() {
        // given
        final Member member = MEMBER_A;
        final Long id = orderRepository.save(A_ORDER).getId();

        // when
        final List<Order> memberOrders = orderRepository.findMemberOrders(MEMBER_A);

        // then
        assertThat(memberOrders).hasSize(1);
    }

}
