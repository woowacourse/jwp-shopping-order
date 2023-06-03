package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import cart.domain.event.FirstOrderCouponEvent;
import cart.domain.event.JoinMemberCouponEvent;
import cart.domain.order.BigDecimalConverter;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberCouponServiceTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberCouponService memberCouponService;


    @Test
    @DisplayName("회원 가입 축하 쿠폰을 발행한다.")
    void saveJoinMemberCoupon() {
        // given
        final String 져니_이름 = "journey1";
        final Long 저장된_져니_아이디 = 사용자를_저장한다(져니_이름);
        final JoinMemberCouponEvent 회원_가입_쿠폰_발행_이벤트 = new JoinMemberCouponEvent(저장된_져니_아이디);

        // when
        memberCouponService.saveJoinMemberCoupon(회원_가입_쿠폰_발행_이벤트);

        // then
        final List<MemberCouponDto> 져니_쿠폰들 = memberCouponDao.findMyCouponsByName(져니_이름);
        assertThat(져니_쿠폰들)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword,
                MemberCouponDto::getCouponName, MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed)
            .containsExactly(
                tuple(저장된_져니_아이디, 져니_이름, "password", "신규 가입 축하 쿠폰", 14, 20, false));
    }

    @Test
    @DisplayName("회원 가입 축하 쿠폰 발행 시 이미 발행이 완료되었다면 예외가 발생한다.")
    void saveJoinMemberCoupon_already_issued() {
        // given
        final String 져니_이름 = "journey2";
        final Long 저장된_져니_아이디 = 사용자를_저장한다(져니_이름);
        final JoinMemberCouponEvent 회원_가입_쿠폰_발행_이벤트 = new JoinMemberCouponEvent(저장된_져니_아이디);
        memberCouponService.saveJoinMemberCoupon(회원_가입_쿠폰_발행_이벤트);

        // then
        assertThatThrownBy(() -> memberCouponService.saveJoinMemberCoupon(회원_가입_쿠폰_발행_이벤트))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_ALREADY_EXIST);

        // then
        final List<MemberCouponDto> 져니_쿠폰들 = memberCouponDao.findMyCouponsByName(져니_이름);
        assertThat(져니_쿠폰들)
            .hasSize(1);
    }

    @Test
    @DisplayName("첫 주문 감사 쿠폰을 발행한다.")
    void saveFirstOrderCoupon() {
        // given
        final String 져니_이름 = "journey3";
        final Long 저장된_져니_아이디 = 사용자를_저장한다(져니_이름);
        상품을_주문한다(저장된_져니_아이디);
        final FirstOrderCouponEvent 첫_주문_감사_쿠폰_발행_이벤트 = new FirstOrderCouponEvent(저장된_져니_아이디);

        // when
        memberCouponService.saveFirstOrderCoupon(첫_주문_감사_쿠폰_발행_이벤트);

        // then
        final List<MemberCouponDto> 져니_쿠폰들 = memberCouponDao.findMyCouponsByName(져니_이름);
        assertThat(져니_쿠폰들)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword,
                MemberCouponDto::getCouponName, MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed)
            .containsExactly(
                tuple(저장된_져니_아이디, 져니_이름, "password", "첫 주문 감사 쿠폰", 14, 10, false));
    }

    @Test
    @DisplayName("첫 주문 감사 쿠폰 발행 시 첫 주문이 아니라면 예외가 발생한다.")
    void saveFirstOrderCoupon_not_first_order() {
        // given
        final String 져니_이름 = "journey4";
        final Long 저장된_져니_아이디 = 사용자를_저장한다(져니_이름);
        상품을_주문한다(저장된_져니_아이디);
        상품을_주문한다(저장된_져니_아이디);
        final FirstOrderCouponEvent 첫_주문_감사_쿠폰_발행_이벤트 = new FirstOrderCouponEvent(저장된_져니_아이디);

        // then
        assertThatThrownBy(() -> memberCouponService.saveFirstOrderCoupon(첫_주문_감사_쿠폰_발행_이벤트))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_NOT_FIRST_ORDER);

        // then
        final List<MemberCouponDto> 져니_쿠폰들 = memberCouponDao.findMyCouponsByName(져니_이름);
        assertThat(져니_쿠폰들)
            .hasSize(0);
    }

    @Test
    @DisplayName("첫 주문 감사 쿠폰 발행 시 이미 발행이 완료되었다면 예외가 발생한다.")
    void saveFirstOrderCoupon_already_issued() {
        // given
        final String 져니_이름 = "journey5";
        final Long 저장된_져니_아이디 = 사용자를_저장한다(져니_이름);
        final FirstOrderCouponEvent 첫_주문_감사_쿠폰_발행_이벤트 = new FirstOrderCouponEvent(저장된_져니_아이디);
        상품을_주문한다(저장된_져니_아이디);
        memberCouponService.saveFirstOrderCoupon(첫_주문_감사_쿠폰_발행_이벤트);

        // then
        assertThatThrownBy(() -> memberCouponService.saveFirstOrderCoupon(첫_주문_감사_쿠폰_발행_이벤트))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_ALREADY_EXIST);

        // then
        final List<MemberCouponDto> 져니_쿠폰들 = memberCouponDao.findMyCouponsByName(져니_이름);
        assertThat(져니_쿠폰들)
            .hasSize(1);
    }

    private Long 사용자를_저장한다(final String name) {
        final MemberEntity 져니 = new MemberEntity(name, "password");
        return memberDao.insert(져니);
    }

    private void 상품을_주문한다(final Long 저장된_져니_아이디) {
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, BigDecimalConverter.convert(10000),
            BigDecimalConverter.convert(9000), 3000, 주문_시간);
        orderDao.insert(주문_엔티티);
    }
}
