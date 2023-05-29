package cart.dao;

import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

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

    public List<MemberCouponEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM member_coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);

    }
}
