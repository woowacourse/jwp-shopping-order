package cart.persistence.coupon;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.type.CouponInfo;
import cart.domain.coupon.type.CouponType;
import cart.error.exception.BadRequestException;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CouponJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private CouponJdbcRepository couponJdbcRepository;

	@BeforeEach
	public void setUp() {
		couponJdbcRepository = new CouponJdbcRepository(jdbcTemplate);
	}

	@Test
	void createCoupon() {
		// given
		CouponInfo couponInfo = CouponInfo.of(null, CouponType.PERCENTAGE.name(), "New Coupon", new BigDecimal(10));

		// when
		Long couponId = couponJdbcRepository.createCoupon(couponInfo);

		// then
		assertThat(couponId).isNotNull();
	}

	@Test
	void findAll() {
		// when
		List<CouponInfo> coupons = couponJdbcRepository.findAll();

		// then
		assertThat(coupons).isNotEmpty();
	}

	@Test
	void findCouponById() {
		// given
		Long couponId = 1L; // Use an actual couponId from the database

		// when
		Coupon coupon = couponJdbcRepository.findCouponById(couponId);

		// then
		assertThat(coupon).isNotNull();
		assertThat(coupon.getCouponInfo().getId()).isEqualTo(couponId);
	}

	@Test
	void updateCouponInfo() {
		// given
		Long couponId = 1L;
		final String name = "Updated Coupon";
		CouponInfo newCouponInfo = CouponInfo.of(couponId, CouponType.FIXED_AMOUNT.name(), name,
			new BigDecimal(50));

		// when
		couponJdbcRepository.updateCouponInfo(newCouponInfo);

		// then
		Coupon updatedCoupon = couponJdbcRepository.findCouponById(couponId);
		assertThat(updatedCoupon.getCouponInfo().getId()).isEqualTo(couponId);
		assertThat(updatedCoupon.getCouponInfo().getCouponType()).isEqualTo(CouponType.FIXED_AMOUNT);
		assertThat(updatedCoupon.getCouponInfo().getName()).isEqualTo(name);

	}

	@Test
	void removeCoupon() {
		// given
		Long couponId = 1L;

		// when
		couponJdbcRepository.removeCoupon(couponId);

		// then
		assertThatThrownBy(() -> couponJdbcRepository.findCouponById(couponId))
			.isInstanceOf(BadRequestException.Coupon.class);

	}
}
