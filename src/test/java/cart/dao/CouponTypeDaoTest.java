package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.CouponTypeEntity;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CouponTypeDaoTest {

    private final CouponTypeDao couponTypeDao;
    private List<CouponTypeEntity> couponTypes;

    @Autowired
    public CouponTypeDaoTest(final JdbcTemplate jdbcTemplate) {
        this.couponTypeDao = new CouponTypeDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        couponTypes = couponTypeDao.findAll();
    }

    @DisplayName("쿠폰 유형 아이디로 쿠폰 유형을 조회한다.")
    @Test
    void findById() {
        // given
        final CouponTypeEntity couponType = couponTypes.get(0);
        final Long couponTypeId = couponType.getId();

        // when, then
        couponTypeDao.findById(couponTypeId)
                .ifPresentOrElse(
                        found -> assertThat(found.getId()).isEqualTo(couponTypeId),
                        () -> Assertions.fail("couponType not exist; couponTypeId=" + couponTypeId));
    }
}
