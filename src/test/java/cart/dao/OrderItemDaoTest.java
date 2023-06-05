package cart.dao;

import static cart.fixture.CouponFixture._3만원_이상_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture.쿠폰_엔티티_발급;
import static cart.fixture.MemberFixture.사용자1_엔티티;
import static cart.fixture.OrderItemFixture._주문_상품_아이템_엔티티_생성;
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
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItemEntity = _주문_상품_아이템_엔티티_생성(1000, 1, order.getId());

        // when
        orderItemDao.insert(orderItemEntity);

        // then
        assertThat(orderItemDao.findAllByOrderId(order.getId())).hasSize(1);
    }

    @Test
    void 주문목록을_받아_저장한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));

        // when
        orderItemDao.insertAll(List.of(
                _주문_상품_아이템_엔티티_생성(1000, 1, order.getId()),
                _주문_상품_아이템_엔티티_생성(2000, 2, order.getId())
        ));

        // then
        assertThat(orderItemDao.findAllByOrderId(order.getId())).hasSize(2);
    }

    @Test
    void 주문_id를_입력받아_전체_주문_상품을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity order = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItem1 = orderItemDao.insert(_주문_상품_아이템_엔티티_생성(1000, 1, order.getId()));
        final OrderItemEntity orderItem2 = orderItemDao.insert(_주문_상품_아이템_엔티티_생성(2000, 2, order.getId()));

        // when
        final List<OrderItemEntity> result = orderItemDao.findAllByOrderId(order.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields()
                .isEqualTo(List.of(orderItem1, orderItem2));
    }

    @Test
    void 주문_id목록을_입력받아_전체_주문_상품을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final OrderEntity order1 = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderEntity order2 = orderDao.insert(new OrderEntity(3000L, coupon.getId(), member.getId()));
        final OrderItemEntity orderItem1 = orderItemDao.insert(_주문_상품_아이템_엔티티_생성(1000, 1, order1.getId()));
        final OrderItemEntity orderItem2 = orderItemDao.insert(_주문_상품_아이템_엔티티_생성(2000, 2, order1.getId()));
        final OrderItemEntity orderItem3 = orderItemDao.insert(_주문_상품_아이템_엔티티_생성(3000, 3, order2.getId()));

        // when
        final List<OrderItemEntity> result = orderItemDao.findAllByOrderIds(List.of(order1.getId(), order2.getId()));

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields()
                .isEqualTo(List.of(orderItem1, orderItem2, orderItem3));
    }
}
