package cart.dao;

import static cart.fixture.CouponFixture._3만원_이상_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture.쿠폰_엔티티_발급;
import static cart.fixture.MemberFixture.사용자1_엔티티;
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
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity orderEntity = new OrderEntity(3000L, coupon.getId(), member.getId());

        // when
        final OrderEntity result = orderDao.insert(orderEntity);

        // then
        assertThat(orderDao.findById(result.getId())).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_전체_주문을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
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
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));

        // when
        final Optional<OrderEntity> result = orderDao.findById(order.getId());

        // then
        assertThat(result).isPresent();
    }
}
