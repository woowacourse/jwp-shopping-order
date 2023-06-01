package cart.dao;

import static cart.fixture.TestFixture.COUPON_FIXED_2000;
import static cart.fixture.TestFixture.COUPON_PERCENTAGE_50;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.dao.dto.MemberCouponDto;
import cart.domain.MemberCoupon;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        this.memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    void 사용자_쿠폰을_삽입한다() {
        MemberCoupon memberCoupon = new MemberCoupon(MEMBER_A, COUPON_FIXED_2000);

        Long id = memberCouponDao.insert(memberCoupon);

        assertThat(id).isNotNull();
    }

    @Test
    void 사용자_쿠폰을_업데이트한다() {
        MemberCoupon fixture = MEMBER_A_COUPON_FIXED_2000();
        MemberCoupon memberCoupon = new MemberCoupon(fixture.getId(), fixture.getOwner(), fixture.getCoupon(), true);
        Long id = memberCouponDao.insert(memberCoupon);

        memberCouponDao.update(memberCoupon);

        MemberCouponDto saved = memberCouponDao.selectBy(id);
        assertThat(saved.getIsUsed()).isTrue();
    }

    @Test
    void 사용자_쿠폰을_불러온다() {
        MemberCoupon memberCoupon = new MemberCoupon(MEMBER_A, COUPON_FIXED_2000);

        Long id = memberCouponDao.insert(memberCoupon);

        MemberCouponDto selected = memberCouponDao.selectBy(id);
        assertThat(selected)
                .usingRecursiveComparison()
                .isEqualTo(MemberCouponDto.of(MEMBER_A_COUPON_FIXED_2000()));
    }

    @Test
    void 사용자_쿠폰을_모두_불러온다() {
        List<MemberCoupon> memberCoupons = List.of(
                new MemberCoupon(MEMBER_A, COUPON_FIXED_2000),
                new MemberCoupon(MEMBER_A, COUPON_PERCENTAGE_50),
                new MemberCoupon(MEMBER_A, COUPON_PERCENTAGE_50)
        );
        List<Long> ids = memberCoupons.stream()
                .map(it -> memberCouponDao.insert(it))
                .collect(Collectors.toList());

        List<MemberCouponDto> memberCouponDtos = memberCouponDao.selectAllBy(MEMBER_A.getId());
        assertThat(memberCouponDtos)
                .extracting("id")
                .containsOnlyOnceElementsOf(ids);
    }
}
