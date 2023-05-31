package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void create() {
        //given
        final Member member = memberDao.findByEmail("kangsj9665@gmail.com");
        final OrderEntity orderEntity = OrderEntity.toCreate(member.getId(), 300);

        //when
        final Long id = orderDao.create(orderEntity);

        //then
        final OrderEntity savedOrder = orderDao.findById(id).get();
        assertAll(
                () -> assertThat(savedOrder.getMemberId()).isEqualTo(member.getId()),
                () -> assertThat(savedOrder.getUsedPoint()).isEqualTo(300)
        );
    }
}
