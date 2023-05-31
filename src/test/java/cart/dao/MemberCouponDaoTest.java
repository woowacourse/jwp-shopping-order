package cart.dao;

import static cart.fixture.CouponFixture.천원_할인_쿠폰;
import static cart.fixture.JdbcTemplateFixture.insertCoupon;
import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertMemberCoupon;
import static cart.fixture.MemberFixture.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.entity.MemberCouponEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("MemberCouponDao 은(는)")
@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    void 멤버_쿠폰을_저장한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        MemberCouponEntity memberCouponEntity = new MemberCouponEntity(MEMBER.getId(), 천원_할인_쿠폰.getId());

        // when
        Long actual = memberCouponDao.save(memberCouponEntity);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 멤버_아이디로_쿠폰을_모두_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        insertMemberCoupon(MEMBER, 천원_할인_쿠폰, jdbcTemplate);
        insertMemberCoupon(MEMBER, 천원_할인_쿠폰, jdbcTemplate);

        Member otherMember = new Member(2L, "aaa@knu.ac.kr", "password");
        insertMember(otherMember, jdbcTemplate);
        insertMemberCoupon(otherMember, 천원_할인_쿠폰, jdbcTemplate);
        insertMemberCoupon(otherMember, 천원_할인_쿠폰, jdbcTemplate);

        // when
        List<MemberCoupon> actual = memberCouponDao.findAllByMemberId(MEMBER.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);
    }
}
