package cart.dao;

import cart.dao.entity.CouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CouponDaoTest {

    private CouponDao couponDao;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE", PreparedStatement::execute);
        couponDao = new CouponDao(jdbcTemplate, dataSource);
    }

    @Test
    void 쿠폰을_발급한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity(false, 1L, 1L);

        // when
        final Long saveId = couponDao.issue(couponEntity);

        // then
        assertThat(saveId).isNotNull();
    }
}
