package cart.dao;

import cart.domain.Coupon;
import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) -> new Coupon(
            rs.getInt("id"),
            rs.getInt("discount"),
            rs.getString("name")
    );

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Coupon> findCouponById(final long memberId) {
        final String findUserCouponSql = "select coupon_id from user_coupon where member_id = ?";
        final List<Long> userCouponIds = jdbcTemplate.queryForList(findUserCouponSql, Long.class, memberId);
        return allCoupon().stream()
                .filter(coupon -> userCouponIds.contains(coupon.getId()))
                .collect(Collectors.toList());
    }

    private List<Coupon> allCoupon() {
        final String allCouponSql = "select * from coupon";
        return jdbcTemplate.query(allCouponSql, couponRowMapper);
    }


    public void deleteUserCoupon(final Member member, final Long couponId) {
        final String sql = "delete from user_coupon where coupon_id = ?";
        jdbcTemplate.update(sql, couponId);
    }
}
