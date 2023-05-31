package cart.dao;

import cart.dto.MemberCouponDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = (rs, rn) -> new MemberCouponDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(MemberCouponDto memberCouponDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberCouponDto.getMemberId());
        params.put("coupon_id", memberCouponDto.getCouponId());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<MemberCouponDto> findById(Long id) {
        String sql = "SELECT * FROM member_coupon WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberCouponDtoRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
