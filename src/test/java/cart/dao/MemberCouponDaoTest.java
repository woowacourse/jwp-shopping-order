package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.dao.dto.OrderDto;
import cart.dto.MemberCouponDto;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@JdbcTest
class MemberCouponDaoTest {

    private final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = (rs, rn) -> new MemberCouponDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"));

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void beforeEach() {
        memberCouponDao = new MemberCouponDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
    }

    @Test
    @DisplayName("Member Coupon 저장을 확인한다.")
    void createMemberCoupon_success() {
        MemberCouponDto saveCouponDto = new MemberCouponDto(1L, 2L, 3L);

        Long insert = memberCouponDao.insert(saveCouponDto);

        String sql = "SELECT * FROM member_coupon where id = ?";
        MemberCouponDto memberCouponDto = jdbcTemplate.queryForObject(
                sql,
                memberCouponDtoRowMapper,
                insert);
        assertThat(memberCouponDto).extracting(
                MemberCouponDto::getId,
                MemberCouponDto::getMemberId,
                MemberCouponDto::getCouponId
        ).contains(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Member Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        MemberCouponDto memberCouponDto = new MemberCouponDto(1L, 2L, 3L);
        String sql = "INSERT INTO member_coupon(id, member_id, coupon_id) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, 1L, 2L, 3L);
        MemberCouponDto queryResult = memberCouponDao.findById(1L).orElseThrow(IllegalArgumentException::new);
        assertThat(queryResult.getId()).isEqualTo(1L);
        assertThat(queryResult.getMemberId()).isEqualTo(2L);
        assertThat(queryResult.getCouponId()).isEqualTo(3L);
    }

}