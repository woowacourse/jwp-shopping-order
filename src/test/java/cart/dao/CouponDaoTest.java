package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import cart.dto.CouponDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@JdbcTest
class CouponDaoTest {

    private final RowMapper<CouponDto> couponDtoRowMapper = (rs, rn) -> new CouponDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDouble("discount_rate"),
            rs.getInt("discount_price")
    );

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private CouponDao couponDao;

    @BeforeEach
    void beforeEach() {
        couponDao = new CouponDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
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
                CouponDto::getDiscountPrice
        ).contains(1L, "홍실 할인", 2d, 0);
    }

    @Test
    @DisplayName("Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        String sql = "INSERT INTO coupon(id, name, discount_rate, discount_price) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, 1L, "홍실 할인", 2d, 0);
        CouponDto queryResult = couponDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(queryResult.getId()).isEqualTo(1L);
        assertThat(queryResult.getName()).isEqualTo("홍실 할인");
        assertThat(queryResult.getDiscountRate()).isEqualTo(2d);
        assertThat(queryResult.getDiscountPrice()).isZero();
    }

    @Test
    @DisplayName("찾는 Coupon Dto 가 없는 경우 빈 Optional 을 반환한다.")
    void findById_returnEmpty() {
        Optional<CouponDto> couponDto = couponDao.findById(1L);
        assertThat(couponDto).isNotPresent();
    }

    @Test
    @DisplayName("쿠폰 전체 조회")
    void findAll() {
        CouponDto rateCoupon = new CouponDto(1L, "홍실 할인", 2d, 0);
        CouponDto priceCoupon = new CouponDto(2L, "홍실 할인", 0d, 2000);
        saveCoupon(rateCoupon);
        saveCoupon(priceCoupon);

        List<CouponDto> couponDtos = couponDao.findAll();
        assertThat(couponDtos).hasSize(2)
                .extracting(CouponDto::getId, CouponDto::getName, CouponDto::getDiscountRate, CouponDto::getDiscountPrice)
                .containsExactly(tuple(1L, "홍실 할인", 2d, 0), Tuple.tuple(2L, "홍실 할인", 0d, 2000));
    }

    private void saveCoupon(CouponDto couponDto) {
        Map<String, Object> params = Map.of(
            "id", couponDto.getId(),
            "name", couponDto.getName(),
            "discount_rate", couponDto.getDiscountRate(),
            "discount_price", couponDto.getDiscountPrice()
        );

        simpleJdbcInsert.executeAndReturnKey(params);
    }

}