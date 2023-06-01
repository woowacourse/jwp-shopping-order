package cart.dao.order;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.MemberFixture.ako;
import static cart.fixture.MemberFixture.ddoring;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
@Import({OrderDao.class, MemberDao.class})
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    private Member member1;
    private Member member2;

    @BeforeEach
    void clean() {
        Long akoId = memberDao.addMember(ako);
        Long ddoringId = memberDao.addMember(ddoring);

        member1 = memberDao.getMemberById(akoId);
        member2 = memberDao.getMemberById(ddoringId);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void insert_order_data() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member1, orderItems);
        order.calculatePrice();

        // when
        Long result = orderDao.insertOrder(order);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("memberId를 통해 member")
    void find_by_memberId() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order1 = new Order(member1, orderItems);
        order1.calculatePrice();
        Order order2 = new Order(member2, orderItems);
        order2.calculatePrice();

        orderDao.insertOrder(order1);
        orderDao.insertOrder(order2);

        // when
        List<OrderEntity> result = orderDao.findByMemberId(member1.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

}