package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import cart.support.MemberTestSupport;
import cart.support.OrderHistoryTestSupport;
import cart.support.OrderItemTestSupport;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@Import({MemberTestSupport.class, OrderHistoryTestSupport.class, MemberDao.class, OrderHistoryDao.class,
        OrderItemTestSupport.class, OrderItemDao.class})
class OrderHistoryDaoTest {

    private OrderHistoryDao orderHistoryDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberTestSupport memberTestSupport;
    @Autowired
    private OrderHistoryTestSupport orderHistoryTestSupport;
    @Autowired
    private OrderItemTestSupport orderItemTestSupport;

    @BeforeEach
    void init() {
        orderHistoryDao = new OrderHistoryDao(jdbcTemplate);
    }

    @DisplayName("주문을 추가하면 생성된 주문의 ID를 반환한다.")
    @Test
    void addOrderHistory() {
        //given
        Member member = memberTestSupport.builder().build();
        OrderHistory orderHistory = orderHistoryTestSupport.builder().member(member).build();
        //when
        Long orderHistoryId = orderHistoryDao.insert(orderHistory);
        //then
        assertThat(orderHistoryId).isNotNull();
    }

    @DisplayName("회원으로 이전 주문 내역을 조회한다.")
    @Test
    void findAllByMemberId() {
        //given
        Member member = memberTestSupport.builder().build();
        OrderHistory orderHistory1 = orderHistoryTestSupport.builder().member(member).build();
        OrderHistory orderHistory2 = orderHistoryTestSupport.builder().member(member).build();
        OrderItem orderItem = orderItemTestSupport.builder().orderHistory(orderHistory1).build();
        orderItemTestSupport.builder().orderHistory(orderHistory2).build();
        orderItemTestSupport.builder().orderHistory(orderHistory2).build();

        //when
        List<OrderHistory> orderHistories = orderHistoryDao.findAllByMemberId(member.getId());

        //then
        assertAll(
                () -> assertThat(orderHistories.size()).isEqualTo(2),
                () -> assertThat(orderHistories.get(0).getOrderItems().size()).isEqualTo(1),
                () -> assertThat(orderHistories.get(1).getOrderItems().size()).isEqualTo(2),
                () -> assertThat(orderHistories.get(0).getMember()).isEqualTo(member),
                () -> assertThat(orderHistories.get(0).getOrderItems().get(0)).isEqualTo(orderItem)
        );
    }

    @DisplayName("주문 ID로 주문 내역을 조회한다.")
    @Test
    void findById() {
        //given
        Member member = memberTestSupport.builder().build();
        OrderHistory orderHistory = orderHistoryTestSupport.builder().member(member).build();
        OrderItem orderItem1 = orderItemTestSupport.builder().orderHistory(orderHistory).build();
        OrderItem orderItem2 = orderItemTestSupport.builder().orderHistory(orderHistory).build();

        //when
        OrderHistory findOrderHistory = orderHistoryDao.findById(orderHistory.getId());

        //then
        assertThat(orderHistory).isEqualTo(findOrderHistory);
    }
}