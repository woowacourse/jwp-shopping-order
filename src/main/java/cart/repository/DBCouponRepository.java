package cart.repository;

import cart.domain.Coupon;
import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class DBCouponRepository implements CouponRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) ->
            new Coupon(rs.getLong("coupon.id"),
                    rs.getString("coupon.name"),
                    rs.getInt("coupon.discount_value"),
                    rs.getInt("coupon.minimum_order_amount"),
                    rs.getTimestamp("coupon.end_date").toLocalDateTime());

    public DBCouponRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.discount_value, coupon.minimum_order_amount, coupon.end_date " +
                "FROM member_coupon " +
                "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                "WHERE member_coupon.member_id = ?";

        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }

    @Override
    public Coupon findById(Long couponId) {
        String sql = "SELECT id, name, discount_value, minimum_order_amount, end_date " +
                "FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, couponRowMapper, couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO coupon (name, discount_value, minimum_order_amount, end_date) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, coupon.getName());
            ps.setInt(2, coupon.getDiscountValue());
            ps.setInt(3, coupon.getMinimumOrderAmount());
            ps.setTimestamp(4, Timestamp.valueOf(coupon.getEndDate()));
            return ps;
        }, keyHolder);

        Long couponId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Coupon(couponId, coupon.getName(), coupon.getDiscountValue(), coupon.getMinimumOrderAmount(), coupon.getEndDate());
    }

    @Override
    public void saveToMember(Member member, Coupon coupon) {
        if (coupon.getId() == null) {
            coupon = save(coupon);
        }

        String sql = "INSERT INTO member_coupon (member_id, coupon_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getId(), coupon.getId());
    }

    @Override
    public void deleteMemberCoupon(Long memberId, Long couponId) {
        String sql = "DELETE FROM member_coupon where member_id = ? AND coupon_id = ?";
        jdbcTemplate.update(sql, memberId, couponId);
    }
}
