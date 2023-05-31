package cart.dao;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.dto.CouponDto;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    @Test
    @DisplayName("Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        String sql = "INSERT INTO coupon(id, name, discount_rate, discount_charge) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, 1L, "홍실 할인", 2d, 0);
        CouponDto queryResult = couponDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(queryResult.getId()).isEqualTo(1L);
        assertThat(queryResult.getName()).isEqualTo("홍실 할인");
        assertThat(queryResult.getDiscountRate()).isEqualTo(2d);
        assertThat(queryResult.getDiscountCharge()).isZero();
    }

    @Test
    @DisplayName("찾는 Coupon Dto 가 없는 경우 빈 Optional 을 반환한다.")
    void findById_returnEmpty() {
        Optional<CouponDto> couponDto = couponDao.findById(1L);
        assertThat(couponDto).isNotPresent();
    }

}