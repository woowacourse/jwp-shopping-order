package cart.dao.order;

import cart.dao.member.MemberDao;
import cart.domain.bill.Bill;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.MemberFixture.ako;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
@Import({OrderItemDao.class, OrderDao.class, MemberDao.class})
class OrderItemDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void clean() {
        Long akoId = memberDao.addMember(ako);

        MemberEntity memberbyId = memberDao.getMemberById(akoId).get();
        member = makeMember(memberbyId);
    }

    private Member makeMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                Rank.valueOf(memberEntity.getGrade()),
                memberEntity.getTotalPurchaseAmount());
    }


    @Test
    @DisplayName("주문 상품들을 저장한다.")
    void insert_orderItems() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);
        Bill bill = order.makeBill();

        Long orderId = orderDao.insertOrder(order, bill);

        // when
        orderItemDao.insert(order.getOrderItems().getOrderItems(), orderId);
        List<OrderItem> result = orderItemDao.findByOrderId(orderId);

        // then
        assertThat(result.size()).isEqualTo(order.getOrderItems().getOrderItems().size());
    }
}