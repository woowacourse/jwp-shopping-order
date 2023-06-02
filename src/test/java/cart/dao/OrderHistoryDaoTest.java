package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.support.MemberTestSupport;
import cart.support.OrderHistoryTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@Import({MemberTestSupport.class, OrderHistoryTestSupport.class, MemberDao.class, OrderHistoryDao.class})
class OrderHistoryDaoTest {

    private OrderHistoryDao orderHistoryDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberTestSupport memberTestSupport;
    @Autowired
    private OrderHistoryTestSupport orderHistoryTestSupport;

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
}