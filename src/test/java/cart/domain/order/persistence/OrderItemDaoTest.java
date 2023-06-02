package cart.domain.order.persistence;

import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.OrderItemFixtures.*;

import java.util.List;

import cart.domain.order.domain.OrderItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        this.orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("멤버 ID에 해당하는 모든 행을 조회한다.")
    void selectAllByMemberId() {
        // given
        Long memberId = Dooly.ID;

        // when
        List<OrderItem> orderItems = orderItemDao.selectAllByMemberId(memberId);

        // then
        Assertions.assertThat(orderItems)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("order.createdAt")
                .containsExactly(Dooly_Order_Item3.ENTITY(), Dooly_Order_Item2.ENTITY(), Dooly_Order_Item1.ENTITY());
    }
}
