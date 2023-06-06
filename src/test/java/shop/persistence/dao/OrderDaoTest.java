package shop.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({OrderDao.class, MemberDao.class, ProductDao.class})
class OrderDaoTest extends DaoTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private static Long memberId;


    @BeforeEach
    void setUp() {
        memberId = memberDao.insertMember(DaoTestFixture.member);
        productDao.insert(DaoTestFixture.chicken);
        productDao.insert(DaoTestFixture.pizza);
    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void createOrderTest() {
        //given
        OrderEntity orderEntity = new OrderEntity(memberId, 10000L,
                0L, 3000, LocalDateTime.now());

        //when
        Long orderId = orderDao.insert(orderEntity);

        //then
        OrderEntity findOrder = orderDao.findById(orderId);

        assertThat(findOrder.getTotalPrice()).isEqualTo(orderEntity.getTotalPrice());
        assertThat(findOrder.getDeliveryPrice()).isEqualTo(orderEntity.getDeliveryPrice());
        assertThat(findOrder.getMemberId()).isEqualTo(orderEntity.getMemberId());
        assertThat(findOrder.getDiscountedTotalPrice()).isEqualTo(orderEntity.getDiscountedTotalPrice());
    }

    @DisplayName("특정 회원의 모든 주문을 조회할 수 있다.")
    @Test
    void findAllByMemberIdTest() {
        //given
        OrderEntity orderEntity1 = new OrderEntity(memberId, 10000L,
                0L, 3000, LocalDateTime.now());
        OrderEntity orderEntity2 = new OrderEntity(memberId, 20000L,
                10000L, 3000, LocalDateTime.now());

        //when
        orderDao.insert(orderEntity1);
        orderDao.insert(orderEntity2);

        //then
        List<OrderEntity> allOrders = orderDao.findAllByMemberId(memberId);

        assertThat(allOrders.size()).isEqualTo(2);
        assertThat(allOrders).extractingResultOf("getDeliveryPrice")
                .containsExactlyInAnyOrder(3000, 3000);
        assertThat(allOrders).extractingResultOf("getTotalPrice")
                .containsExactlyInAnyOrder(10000L, 20000L);
        assertThat(allOrders).extractingResultOf("getMemberId")
                .containsExactlyInAnyOrder(memberId, memberId);
        assertThat(allOrders).extractingResultOf("getDiscountedTotalPrice")
                .containsExactlyInAnyOrder(0L, 10000L);
    }
}
