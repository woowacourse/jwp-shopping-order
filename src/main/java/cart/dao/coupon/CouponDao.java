package cart.dao.coupon;

import cart.entity.coupon.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CouponDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) ->
            new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("policy_id"),
                    rs.getLong("member_id")
            );

    public List<CouponEntity> findAllCouponEntitiesByMemberId(final long memberId) {
        String sql = "SELECT * FROM coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public CouponEntity findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, couponId);
    }

    public void deleteById(final long couponId) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, couponId);
    }
}
