package cart.dao;

import cart.dao.entity.MemberCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberCouponDao {
    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) -> new MemberCouponEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"),
            rs.getDate("expired_at")
    );

    public List<MemberCouponEntity> findUsableByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM member_coupon WHERE is_used = false AND member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);

    }
}
