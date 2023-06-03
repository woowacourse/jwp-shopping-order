package com.woowahan.techcourse.coupon.acceptance;

import static com.woowahan.techcourse.common.acceptance.CommonSteps.비정상_요청;
import static com.woowahan.techcourse.common.acceptance.CommonSteps.요청_결과의_상태를_검증한다;
import static com.woowahan.techcourse.common.acceptance.CommonSteps.정상_요청;

import com.woowahan.techcourse.common.acceptance.IntegrationTest;
import com.woowahan.techcourse.member.dao.MemberDao;
import com.woowahan.techcourse.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
class CouponAcceptanceTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setDummyData() {
        super.setUp();
        long memberId3 = memberDao.insert(new Member(3L, "3@woowahan.com", "password"));
        memberDao.insert(new Member(4L, "4@woowahan.com", "password"));

        jdbcTemplate.execute("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.execute(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.execute("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.execute(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
        jdbcTemplate.execute("INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, " + memberId3 + ")");
    }

    @Test
    void 전체_쿠폰_목록을_조회할_수_있다() {
        var 결과 = CouponStep.쿠폰_전체_조회();

        CouponStep.쿠폰_사이즈는_N이다(결과, 1);
        요청_결과의_상태를_검증한다(결과, 정상_요청);
    }

    @Test
    void 쿠폰이_없는_멤버의_쿠폰을_조회할_수_있다() {
        var 결과 = CouponStep.멤버의_보유_쿠폰_조회("4@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(결과, 0);
        요청_결과의_상태를_검증한다(결과, 정상_요청);
    }

    @Test
    void 쿠폰이_있는_멤버의_쿠폰을_조회할_수_있다() {
        var 결과 = CouponStep.멤버의_보유_쿠폰_조회("3@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(결과, 1);
        요청_결과의_상태를_검증한다(결과, 정상_요청);
    }

    @Test
    void 쿠폰을_등록하면_쿠폰이_추가된다() {
        var 사용_전_보유_쿠폰 = CouponStep.멤버의_보유_쿠폰_조회("4@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(사용_전_보유_쿠폰, 0);
        요청_결과의_상태를_검증한다(사용_전_보유_쿠폰, 정상_요청);

        var 쿠폰_사용_결과 = CouponStep.쿠폰_획득(1L, "4@woowahan.com", "password");

        요청_결과의_상태를_검증한다(쿠폰_사용_결과, 정상_요청);

        var 사용후_보유_쿠폰 = CouponStep.멤버의_보유_쿠폰_조회("4@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(사용후_보유_쿠폰, 1);
        요청_결과의_상태를_검증한다(사용후_보유_쿠폰, 정상_요청);
    }

    @Test
    void 쿠폰을_중복_등록하면_예외가_발생한다() {
        var 사용_전_보유_쿠폰 = CouponStep.멤버의_보유_쿠폰_조회("3@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(사용_전_보유_쿠폰, 1);
        요청_결과의_상태를_검증한다(사용_전_보유_쿠폰, 정상_요청);

        var 쿠폰_사용_결과 = CouponStep.쿠폰_획득(1L, "3@woowahan.com", "password");

        요청_결과의_상태를_검증한다(쿠폰_사용_결과, 비정상_요청);

        var 사용_후_보유_쿠폰 = CouponStep.멤버의_보유_쿠폰_조회("3@woowahan.com", "password");

        CouponStep.쿠폰_사이즈는_N이다(사용_후_보유_쿠폰, 1);
        요청_결과의_상태를_검증한다(사용_후_보유_쿠폰, 정상_요청);
    }

    @Test
    void 없는_쿠폰을_등록하면_예외가_발생한다() {
        var 결과 = CouponStep.쿠폰_획득(2L, "3@woowahan.com", "password");

        요청_결과의_상태를_검증한다(결과, 비정상_요청);
    }
}
