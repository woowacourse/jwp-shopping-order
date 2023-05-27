package cart.dao;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CouponEntity;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderItemDaoTest {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberDao memberDao;

    @Test
    void 주문을_저장한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItemEntity = new OrderItemEntity("허브티", "tea.jpg", 1000L, 4, order.getId());

        // when
        orderItemDao.insert(orderItemEntity);

        // then
        assertThat(orderItemDao.findAllByOrderId(order.getId())).hasSize(1);
    }

    @Test
    void 주문목록을_받아_저장한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));

        // when
        orderItemDao.insertAll(List.of(
                new OrderItemEntity("허브티", "tea.jpg", 1000L, 1, order.getId()),
                new OrderItemEntity("공차", "tea.jpg", 1000L, 2, order.getId())
        ));

        // then
        assertThat(orderItemDao.findAllByOrderId(order.getId())).hasSize(2);
    }

    @Test
    void 주문_id를_입력받아_전체_주문_상품을_조회한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItem1 = orderItemDao.insert(
                new OrderItemEntity("허브티", "tea.jpg", 1000L, 4, order.getId())
        );
        final OrderItemEntity orderItem2 = orderItemDao.insert(
                new OrderItemEntity("공차", "tea.jpg", 1000L, 4, order.getId())
        );

        // when
        final List<OrderItemEntity> result = orderItemDao.findAllByOrderId(order.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields()
                .isEqualTo(List.of(orderItem1, orderItem2));
    }

    @Test
    void 주문_id목록을_입력받아_전체_주문_상품을_조회한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order1 = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderEntity order2 = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItem1 = orderItemDao.insert(
                new OrderItemEntity("허브티", "tea.jpg", 1000L, 1, order1.getId())
        );
        final OrderItemEntity orderItem2 = orderItemDao.insert(
                new OrderItemEntity("공차", "tea.jpg", 1000L, 2, order1.getId())
        );
        final OrderItemEntity orderItem3 = orderItemDao.insert(
                new OrderItemEntity("말차", "tea.jpg", 1000L, 3, order2.getId())
        );

        // when
        final List<OrderItemEntity> result = orderItemDao.findAllByOrderIds(List.of(order1.getId(), order2.getId()));

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields()
                .isEqualTo(List.of(orderItem1, orderItem2, orderItem3));
    }
}
