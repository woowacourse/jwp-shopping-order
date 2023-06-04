package cart.dao;

import static cart.fixture.CouponFixture.천원_할인_쿠폰;
import static cart.fixture.JdbcTemplateFixture.insertCoupon;
import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertMemberCoupon;
import static cart.fixture.JdbcTemplateFixture.insertOrder;
import static cart.fixture.JdbcTemplateFixture.insertOrderItem;
import static cart.fixture.JdbcTemplateFixture.insertProduct;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.OrderItemFixture.getOrderItemEntity;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThatNoException;

import cart.domain.MemberCoupon;
import cart.entity.OrderCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderCouponDao 은(는)")
@JdbcTest
class OrderCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderCouponDao orderCouponDao;

    @BeforeEach
    void setUp() {
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
    }

    @Test
    void 배치_세이브_테스트() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        MemberCoupon memberCouponA = new MemberCoupon(1L, MEMBER.getId(), 천원_할인_쿠폰, false);
        MemberCoupon memberCouponB = new MemberCoupon(2L, MEMBER.getId(), 천원_할인_쿠폰, false);
        insertMemberCoupon(memberCouponA, jdbcTemplate);
        insertMemberCoupon(memberCouponB, jdbcTemplate);

        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId(), 10000);
        OrderItemEntity orderItemChicken = getOrderItemEntity(1L, orderEntity.getId(), 10, CHICKEN);
        OrderItemEntity orderItemPizza = getOrderItemEntity(2L, orderEntity.getId(), 5, PIZZA);
        insertOrder(orderEntity, jdbcTemplate);
        insertOrderItem(orderItemChicken, jdbcTemplate);
        insertOrderItem(orderItemPizza, jdbcTemplate);
        List<OrderCouponEntity> orderCouponEntities = List.of(
                new OrderCouponEntity(orderItemChicken.getId(), memberCouponA.getId()),
                new OrderCouponEntity(orderItemPizza.getId(), memberCouponB.getId()));

        // when & then
        assertThatNoException().isThrownBy(() -> orderCouponDao.batchSave(orderCouponEntities));
    }
}
