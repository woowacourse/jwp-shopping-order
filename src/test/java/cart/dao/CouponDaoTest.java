package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@JdbcTest
class CouponDaoTest {

    private final RowMapper<CouponDto> couponDtoRowMapper = (rs, rn) -> new CouponDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDouble("discount_rate"),
            rs.getInt("discount_charge"));

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private CouponDao couponDao;

    @BeforeEach
    void beforeEach() {
        couponDao = new CouponDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
    }

    @Test
    @DisplayName("CouponDao 를 이용해 Coupon 을 저장한다.")
    void insert() {
        CouponDto couponDto = new CouponDto(1L, "홍실 할인", 2d, 0);

        Long insert = couponDao.insert(couponDto);

        String sql = "SELECT * FROM coupon where id = ?";
        CouponDto queryResultDto = jdbcTemplate.queryForObject(
                sql,
                couponDtoRowMapper,
                insert);
        assertThat(queryResultDto).extracting(
                CouponDto::getId,
                CouponDto::getName,
                CouponDto::getDiscountRate,
                CouponDto::getDiscountCharge
        ).contains(1L, "홍실 할인", 2d, 0);
    }

}