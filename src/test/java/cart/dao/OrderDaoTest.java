package cart.dao;

import cart.domain.Member;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class OrderDaoTest {
    private OrderDao orderDao;
    private MemberDao memberDao;
    private OrderProductDao orderProductDao;
    private ProductDao productDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        orderProductDao = new OrderProductDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void saveOrder() {
        Member member = memberDao.getMemberById(1L);
        final OrderEntity orderEntity = new OrderEntity(member.getId(), member.getId(), 10000, 10000, false);

        assertDoesNotThrow(() -> orderDao.saveOrder(orderEntity));
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void findAllByMember() {
        Member member = memberDao.getMemberById(1L);
        OrderEntity orderEntity = new OrderEntity(member.getId(), member.getId(), 10000, 10000, false);

        assertDoesNotThrow(() -> orderDao.saveOrder(orderEntity));
    }
}
