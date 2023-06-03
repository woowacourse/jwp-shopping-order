package cart.dao.order;

import cart.dao.member.MemberDao;
import cart.domain.bill.Bill;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

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

        MemberEntity member1ById = memberDao.getMemberById(akoId).get();
        MemberEntity member2ById = memberDao.getMemberById(ddoringId).get();

        member1 = makeMember(member1ById);
        member2 = makeMember(member2ById);
    }

    private Member makeMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                Rank.valueOf(memberEntity.getGrade()),
                memberEntity.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void insert_order_data() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member1, orderItems);
        Bill bill = order.makeBill();

        // when
        Long id = orderDao.insertOrder(order, bill);
        Optional<OrderEntity> result = orderDao.findById(id);

        // then
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getDiscountedTotalItemPrice()).isEqualTo(bill.getDiscountedTotalItemPrice());
        assertThat(result.get().getShippingFee()).isEqualTo(bill.getShippingFee());
        assertThat(result.get().getTotalItemPrice()).isEqualTo(bill.getTotalPrice());

    }

    @Test
    @DisplayName("memberId를 통해 orderEntity를 찾는다.")
    void find_by_member_id() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order1 = new Order(member1, orderItems);
        Bill bill1 = order1.makeBill();
        Order order2 = new Order(member2, orderItems);
        Bill bill2 = order2.makeBill();

        orderDao.insertOrder(order1, bill1);
        orderDao.insertOrder(order2, bill2);

        // when
        List<OrderEntity> result = orderDao.findByMemberId(member1.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("orderId를 통해 orderEntity를 찾는다.")
    void find_by_order_id() {
        // given
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order1 = new Order(member1, orderItems);
        Bill bill1 = order1.makeBill();
        Order order2 = new Order(member2, orderItems);
        Bill bill2 = order2.makeBill();

        Long order1Id = orderDao.insertOrder(order1, bill1);
        Long order2Id = orderDao.insertOrder(order2, bill2);

        // when
        Optional<OrderEntity> result = orderDao.findById(order1Id);

        // then
        assertThat(result.get().getTotalItemPrice()).isEqualTo(bill1.getTotalPrice());
        assertThat(result.get().getDiscountedTotalItemPrice()).isEqualTo(bill1.getDiscountedTotalItemPrice());
        assertThat(result.get().getShippingFee()).isEqualTo(bill1.getShippingFee());
    }
}
