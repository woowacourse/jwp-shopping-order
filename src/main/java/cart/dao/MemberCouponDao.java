package cart.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long memberId, long couponId) {
        String sql = "INSERT INTO member_coupon (member_id, coupon_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, memberId, couponId);
    }

    public List<Long> findCouponIdsByMemberId(Long memberId) {
        String sql = "SELECT coupon_id FROM member_coupon WHERE member_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> resultSet.getLong("coupon_id"),
                memberId
        );
    }
}
