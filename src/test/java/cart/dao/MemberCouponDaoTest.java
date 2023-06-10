package cart.dao;

import cart.dao.entity.MemberCouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberCouponDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberCouponDao memberCouponDao;

    @BeforeEach
    public void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    public void 저장한다() {
        MemberCouponEntity memberCoupon = new MemberCouponEntity(
                null,
                1L,
                1L,
                false,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now()
        );

        Long generatedId = memberCouponDao.save(memberCoupon);

        assertThat(generatedId).isNotNull();
    }

    @Test
    public void 회원_아이디로_조회한다() {
        Long memberId = 1L;

        List<MemberCouponEntity> memberCoupons = memberCouponDao.findUsableByMemberId(memberId);

        assertThat(memberCoupons).hasSize(2);
    }

    @Test
    public void 아이디로_조회한다() {
        Long id = 1L;

        Optional<MemberCouponEntity> memberCoupon = memberCouponDao.findById(id);

        assertThat(memberCoupon.get().getMemberId()).isEqualTo(1L);
    }

    @Test
    public void 아이디로_조회_시_없으면_빈값을_반환한다() {
        Long id = 100L;

        Optional<MemberCouponEntity> memberCoupon = memberCouponDao.findById(id);

        assertThat(memberCoupon).isEmpty();
    }

    @Test
    public void 아이디_목록으로_조회한다() {
        List<Long> ids = List.of(1L, 2L);

        List<MemberCouponEntity> memberCoupons = memberCouponDao.findByIds(ids);

        assertThat(memberCoupons).hasSize(2);
    }

    @Test
    public void 수정한다() {
        MemberCouponEntity memberCoupon = new MemberCouponEntity(
                null,
                1L,
                1L,
                false,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now()
        );
        Long generatedId = memberCouponDao.save(memberCoupon);
        MemberCouponEntity savedMemberCoupon = new MemberCouponEntity(
                generatedId,
                1L,
                1L,
                true,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now()
        );

        memberCouponDao.update(savedMemberCoupon);

        Optional<MemberCouponEntity> updatedMemberCoupon = memberCouponDao.findById(generatedId);
        assertThat(updatedMemberCoupon.get().getUsed()).isTrue();
    }

}
