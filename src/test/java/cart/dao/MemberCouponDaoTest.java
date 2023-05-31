package cart.dao;


import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class MemberCouponDaoTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberCouponDao memberCouponDao;

    private MemberDao memberDao;

    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        this.memberCouponDao = new MemberCouponDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    void 사용자_쿠폰을_저장한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");
        final CouponEntity couponEntity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final MemberEntity member = memberDao.insert(memberEntity);
        final CouponEntity coupon = couponDao.insert(couponEntity);
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(member.getId(), coupon.getId(), false);

        // when
        final MemberCouponEntity inserted = memberCouponDao.insert(memberCouponEntity);

        // then
        final MemberCouponEntity result = memberCouponDao.findById(inserted.getId()).get();
        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(inserted.getMemberId()),
                () -> assertThat(result.getCouponId()).isEqualTo(inserted.getCouponId()),
                () -> assertThat(result.isUsed()).isFalse()
        );
    }

    @Test
    void 사용자_쿠폰을_수정한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");
        final CouponEntity couponEntity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final MemberEntity member = memberDao.insert(memberEntity);
        final CouponEntity coupon = couponDao.insert(couponEntity);
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(member.getId(), coupon.getId(), false);
        final MemberCouponEntity memberCoupon = memberCouponDao.insert(memberCouponEntity);
        final MemberCouponEntity updatedEntity = new MemberCouponEntity(memberCoupon.getId(), memberCoupon.getMemberId(), memberCoupon.getCouponId(), true);

        // when
        memberCouponDao.update(updatedEntity);

        // then
        final MemberCouponEntity result = memberCouponDao.findById(memberCoupon.getId()).get();
        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(updatedEntity.getMemberId()),
                () -> assertThat(result.getCouponId()).isEqualTo(updatedEntity.getCouponId()),
                () -> assertThat(result.isUsed()).isEqualTo(updatedEntity.isUsed())
        );
    }

    @Test
    void 사용자_아이디에_해당하는_사용자_쿠폰을_조회한다() {
        // given
        final MemberEntity memberEntity1 = new MemberEntity("pizza1@pizza.com", "password");
        final MemberEntity memberEntity2 = new MemberEntity("pizza2@pizza.com", "password");
        final CouponEntity couponEntity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final MemberEntity member1 = memberDao.insert(memberEntity1);
        final MemberEntity member2 = memberDao.insert(memberEntity2);
        final CouponEntity coupon = couponDao.insert(couponEntity);
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(member1.getId(), coupon.getId(), false);
        memberCouponDao.insert(memberCouponEntity);
        memberCouponDao.insert(memberCouponEntity);

        // when
        final List<MemberCouponEntity> result = memberCouponDao.findByMemberId(member1.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void 아이디에_해당하는_사용자_쿠폰을_조회한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "password");
        final CouponEntity couponEntity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final MemberEntity insertedMemberEntity = memberDao.insert(memberEntity);
        final CouponEntity insertedCouponEntity = couponDao.insert(couponEntity);
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(insertedMemberEntity.getId(), insertedCouponEntity.getId(), false);
        final MemberCouponEntity inserted = memberCouponDao.insert(memberCouponEntity);

        // when
        final MemberCouponEntity result = memberCouponDao.findById(inserted.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(inserted.getMemberId()),
                () -> assertThat(result.getCouponId()).isEqualTo(inserted.getCouponId()),
                () -> assertThat(result.isUsed()).isEqualTo(inserted.isUsed())
        );
    }
}