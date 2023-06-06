package shop.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.CouponEntity;
import shop.persistence.entity.MemberCouponEntity;
import shop.persistence.entity.MemberEntity;
import shop.persistence.entity.detail.MemberCouponDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({MemberDao.class, CouponDao.class, MemberCouponDao.class})
class MemberCouponDaoTest extends DaoTest {
    private static Long memberId;
    private static Long couponId1;
    private static Long couponId2;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberId = memberDao.insertMember(Data.member);
        couponId1 = couponDao.insert(Data.coupon1);
        couponId2 = couponDao.insert(Data.coupon2);
    }

    @DisplayName("회원의 쿠폰을 저장할 수 있다.")
    @Test
    void insertMemberCouponTest() {
        //given
        MemberCouponEntity memberCoupon = new MemberCouponEntity(memberId, couponId1,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);

        //when
        memberCouponDao.insert(memberCoupon);

        //then
        MemberCouponDetail findMemberCoupon = memberCouponDao.findByMemberIdAndCouponId(memberId, couponId1);

        assertThat(findMemberCoupon.getCouponName()).isEqualTo(Data.coupon1.getName());
        assertThat(findMemberCoupon.getMemberName()).isEqualTo(Data.member.getName());
    }

    @DisplayName("회원의 모든 쿠폰을 조회할 수 있다.")
    @Test
    void findAllMemberCouponTest() {
        MemberCouponEntity memberCoupon1 = new MemberCouponEntity(memberId, couponId1,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);
        MemberCouponEntity memberCoupon2 = new MemberCouponEntity(memberId, couponId2,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);

        //when
        memberCouponDao.insert(memberCoupon1);
        memberCouponDao.insert(memberCoupon2);

        //then
        List<MemberCouponDetail> findMemberCoupons = memberCouponDao.findAllByMemberId(memberId);

        assertThat(findMemberCoupons.size()).isEqualTo(2);
        assertThat(findMemberCoupons).extractingResultOf("getCouponName")
                .containsExactly(Data.coupon1.getName(), Data.coupon2.getName());
        assertThat(findMemberCoupons).extractingResultOf("getMemberName")
                .containsExactly(Data.member.getName(), Data.member.getName());
    }

    private static class Data {
        static final MemberEntity member = new MemberEntity("쥬니", "1234");
        static final CouponEntity coupon1 = new CouponEntity("테스트용 쿠폰", 80,
                365, LocalDateTime.now().plusDays(1));
        static final CouponEntity coupon2 = new CouponEntity("공짜 쿠폰", 100,
                365, LocalDateTime.now().plusDays(1));
    }
}
