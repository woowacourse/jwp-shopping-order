package cart.dao;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CouponEntity;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CouponDao couponDao;

    @Test
    void 주문을_저장한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity orderEntity = new OrderEntity(3000L, coupon.getId(), member.getId());

        // when
        final OrderEntity result = orderDao.insert(orderEntity);

        // then
        assertThat(orderDao.findById(result.getId())).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_전체_주문을_조회한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order1 = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderEntity order2 = orderDao.insert(new OrderEntity(3000L, null, member.getId()));

        // when
        final List<OrderEntity> result = orderDao.findAllByMemberId(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(order1, order2));
    }

    @Test
    void 단일_주문을_조회한다() {
        // given
        final CouponEntity coupon = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza@pizza.com", "password"));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));

        // when
        final Optional<OrderEntity> result = orderDao.findById(order.getId());

        // then
        assertThat(result).isPresent();
    }
}
