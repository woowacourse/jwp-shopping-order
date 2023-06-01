package cart.persistence.coupon;

import cart.domain.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest

class CouponJdbcRepositoryTest {

    private CouponJdbcRepository couponJdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponJdbcRepository = new CouponJdbcRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자의 따른 저장되어있는 쿠폰 조회 테스트")
    void findByMemberId() {
        List<Coupon> coupons = couponJdbcRepository.findByMemberId(2L);
        Assertions.assertThat(coupons).hasSize(3);
    }

}
