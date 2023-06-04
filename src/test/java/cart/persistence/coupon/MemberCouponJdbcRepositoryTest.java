package cart.persistence.coupon;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.coupon.MemberCoupon;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MemberCouponJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private MemberCouponJdbcRepository memberCouponJdbcRepository;

	@BeforeEach
	public void setUp() {
		memberCouponJdbcRepository = new MemberCouponJdbcRepository(jdbcTemplate);
	}

	@Test
	void findByMemberId() {
		// given
		final long memberId = 1L;

		// when
		final MemberCoupon memberCoupon = memberCouponJdbcRepository.findByMemberId(memberId);

		// then
		assertThat(memberCoupon.getCouponInfos()).hasSize(1);
	}

	@Test
	void deleteByCouponId() {
		// given
		final long memberId = 1L;
		final long couponId = 1L;

		// when
		memberCouponJdbcRepository.deleteByMemberIdAndCouponId(memberId, couponId);

		// then
		final MemberCoupon actual = memberCouponJdbcRepository.findByMemberId(1L);
		assertThat(actual.getCouponInfos()).hasSize(0);
	}
}
