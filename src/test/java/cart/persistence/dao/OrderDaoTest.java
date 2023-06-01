package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderCouponEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({OrderProductDao.class, OrderCouponDao.class})
class OrderDaoTest extends DaoTestHelper {

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderCouponDao orderCouponDao;

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void insert() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final Long 저장된_져니_아이디 = 져니_저장();
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, 주문_시간);

        // when
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);

        // then
        final List<OrderDto> 저장된_주문_정보 = orderDao.findById(저장된_주문_아이디);
        assertThat(저장된_주문_정보)
            .extracting(OrderDto::getOrderId, OrderDto::getTotalPrice, OrderDto::getDiscountedTotalPrice,
                OrderDto::getDeliveryPrice, OrderDto::getMemberId, OrderDto::getMemberName, OrderDto::getMemberPassword)
            .containsExactly(
                tuple(저장된_주문_아이디, 10000, 9000, 3000, 저장된_져니_아이디, "journey", "password")
            );
    }

    @Test
    @DisplayName("특정 회원이 주문한 전체 횟수를 구한다.")
    void countByMemberId() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, 주문_시간);
        orderDao.insert(주문_엔티티);

        // when
        final Long 저장된_주문_횟수 = orderDao.countByMemberId(저장된_져니_아이디);

        // then
        assertThat(저장된_주문_횟수)
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 아이디로 주문 정보를 조회한다.")
    void findById() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final Long 저장된_져니_아이디 = 져니_저장();
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 350_000, 280_000, 3_000, 주문_시간);
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);
        final List<Long> 저장된_상품_아이디들 = 주문_상품들을_저장한다(저장된_주문_아이디);
        final Long 저장된_주문_쿠폰_아이디 = 주문_쿠폰을_저장한다(저장된_주문_아이디);

        // when
        final List<OrderDto> 저장된_주문_정보 = orderDao.findById(저장된_주문_아이디);

        // then
        assertThat(저장된_주문_정보)
            .extracting(OrderDto::getOrderId, OrderDto::getOrderedAt, OrderDto::getOrderQuantity,
                OrderDto::getTotalPrice, OrderDto::getDiscountedTotalPrice, OrderDto::getDeliveryPrice,
                OrderDto::getCouponId, OrderDto::getCouponName, OrderDto::getCouponDiscountRate,
                OrderDto::getCouponPeriod, OrderDto::getCouponExpiredAt, OrderDto::getMemberId,
                OrderDto::getMemberName, OrderDto::getMemberPassword, OrderDto::getProductId,
                OrderDto::getProductName, OrderDto::getProductPrice, OrderDto::getProductImageUrl)
            .containsExactly(
                tuple(저장된_주문_아이디, 주문_시간, 10,
                    350_000, 280_000, 3_000,
                    저장된_주문_쿠폰_아이디, "신규 가입 축하 쿠폰", 20,
                    10, 주문_시간.plusDays(10), 저장된_져니_아이디,
                    "journey", "password", 저장된_상품_아이디들.get(0),
                    "치킨", 20000, "chicken_image_url"),
                tuple(저장된_주문_아이디, 주문_시간, 5,
                    350_000, 280_000, 3_000,
                    저장된_주문_쿠폰_아이디, "신규 가입 축하 쿠폰", 20,
                    10, 주문_시간.plusDays(10), 저장된_져니_아이디,
                    "journey", "password", 저장된_상품_아이디들.get(1),
                    "피자", 30000, "pizza_image_url")
            );
    }

    @Test
    @DisplayName("사용자 이름으로 주문 정보를 조회한다.")
    void findByMemberName() {
        // given
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_라온_아이디 = 다른_사용자를_저장한다();

        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 350_000, 280_000, 3_000, 주문_시간);
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);
        다른_사용자의_주문을_추가한다(저장된_라온_아이디);

        final List<Long> 저장된_상품_아이디들 = 주문_상품들을_저장한다(저장된_주문_아이디);
        final Long 저장된_주문_쿠폰_아이디 = 주문_쿠폰을_저장한다(저장된_주문_아이디);

        // when
        final List<OrderDto> 저장된_주문_정보 = orderDao.findByMemberName("journey");

        // then
        assertThat(저장된_주문_정보)
            .extracting(OrderDto::getOrderId, OrderDto::getOrderedAt, OrderDto::getOrderQuantity,
                OrderDto::getTotalPrice, OrderDto::getDiscountedTotalPrice, OrderDto::getDeliveryPrice,
                OrderDto::getCouponId, OrderDto::getCouponName, OrderDto::getCouponDiscountRate,
                OrderDto::getCouponPeriod, OrderDto::getCouponExpiredAt, OrderDto::getMemberId,
                OrderDto::getMemberName, OrderDto::getMemberPassword, OrderDto::getProductId,
                OrderDto::getProductName, OrderDto::getProductPrice, OrderDto::getProductImageUrl)
            .containsExactly(
                tuple(저장된_주문_아이디, 주문_시간, 10,
                    350_000, 280_000, 3_000,
                    저장된_주문_쿠폰_아이디, "신규 가입 축하 쿠폰", 20,
                    10, 주문_시간.plusDays(10), 저장된_져니_아이디,
                    "journey", "password", 저장된_상품_아이디들.get(0),
                    "치킨", 20000, "chicken_image_url"),
                tuple(저장된_주문_아이디, 주문_시간, 5,
                    350_000, 280_000, 3_000,
                    저장된_주문_쿠폰_아이디, "신규 가입 축하 쿠폰", 20,
                    10, 주문_시간.plusDays(10), 저장된_져니_아이디,
                    "journey", "password", 저장된_상품_아이디들.get(1),
                    "피자", 30000, "pizza_image_url")
            );
    }

    private List<Long> 주문_상품들을_저장한다(final Long 저장된_주문_아이디) {
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        final List<OrderProductEntity> 주문_상품_엔티티들 = List.of(
            new OrderProductEntity(저장된_주문_아이디, 저장된_치킨_아이디, 20000, 10),
            new OrderProductEntity(저장된_주문_아이디, 저장된_피자_아이디, 30000, 5));
        orderProductDao.saveAll(주문_상품_엔티티들);
        return List.of(저장된_치킨_아이디, 저장된_피자_아이디);
    }

    private Long 주문_쿠폰을_저장한다(final Long 저장된_주문_아이디) {
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        final OrderCouponEntity 주문_쿠폰_엔티티 = new OrderCouponEntity(저장된_주문_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        return orderCouponDao.insert(주문_쿠폰_엔티티);
    }

    private Long 다른_사용자를_저장한다() {
        final MemberEntity 라온 = new MemberEntity("raon", "password");
        return memberDao.insert(라온);
    }

    private void 다른_사용자의_주문을_추가한다(final Long 저장된_라온_아이디) {
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final OrderEntity 다른_사용자의_주문_엔티티1 = new OrderEntity(저장된_라온_아이디, 10000, 9000, 3000, 주문_시간);
        final OrderEntity 다른_사용자의_주문_엔티티2 = new OrderEntity(저장된_라온_아이디, 20000, 1000, 10000, 주문_시간);
        orderDao.insert(다른_사용자의_주문_엔티티1);
        orderDao.insert(다른_사용자의_주문_엔티티2);
    }
}
