package cart.dao;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.orderproduct.Order;
import cart.dto.OrderRequest;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class OrderDaoTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderService orderService;

    @DisplayName("상품을 주문한다.")
    @Test
    void order() {
        // given
        final Member member = memberDao.getMemberById(1L);
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1000);

        // when
        final Long orderId = orderService.order(member, orderRequest);

        // then
        final Member updatedMember = memberDao.getMemberById(1L);
        final Order order = orderRepository.findOrderById(orderId);
        assertAll(
                () -> assertThat(order.getMember()).isEqualTo(member)
        );

    }
}
