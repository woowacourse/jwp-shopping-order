package cart.dao;

import cart.domain.MemberCoupon;
import cart.dto.MemberCouponDto;
import java.util.HashMap;
import java.util.List;
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

    public Optional<MemberCouponDto> findByIdAndMemberId(Long id, Long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE id = ? AND member_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberCouponDtoRowMapper, id, memberId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<MemberCouponDto> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ?";

        return jdbcTemplate.query(sql, memberCouponDtoRowMapper, memberId);
    }

    public int deleteById(final Long memberCouponId) {
        String sql = "DELETE FROM member_coupon WHERE id = ?";

        return jdbcTemplate.update(sql, memberCouponId);
    }

}
