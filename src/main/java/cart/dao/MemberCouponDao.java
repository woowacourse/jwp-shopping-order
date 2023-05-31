package cart.dao;

import cart.dao.entity.MemberCouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<MemberCouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM member_coupon WHERE id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    //예외 컨트롤
    public void updateUsedById(final Long id) {
        final String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }
}
