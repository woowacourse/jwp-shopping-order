package cart.dao;

import cart.entity.MemberCouponEntity;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private static final RowMapper<MemberCouponEntity> ROW_MAPPER = (rs, rowNum) -> {
        long id = rs.getLong("id");
        long memberId = rs.getLong("member_id");
        long couponId = rs.getLong("coupon_id");
        Date expiredDate = rs.getDate("expired_date");
        return new MemberCouponEntity(id, memberId, couponId, expiredDate.toLocalDate());
    };

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberCouponEntity> findById(Long couponId) {
        String sql = "SELECT * FROM member_coupon WHERE id = ?";
        try {
            MemberCouponEntity memberCoupon = jdbcTemplate.queryForObject(sql, ROW_MAPPER, couponId);
            return Optional.ofNullable(memberCoupon);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(MemberCouponEntity memberCoupon) {
        String sql = "INSERT INTO member_coupon (member_id, coupon_id, expired_date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, memberCoupon.getMemberId());
            ps.setLong(2, memberCoupon.getCouponId());
            ps.setDate(3, Date.valueOf(memberCoupon.getExpiredDate()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM member_coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberCouponEntity> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public void saveAll(List<MemberCouponEntity> memberCoupons) {
        String sql = "INSERT INTO member_coupon (member_id, coupon_id, expired_date) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, memberCoupons, memberCoupons.size(), ((ps, memberCoupon) -> {
            ps.setLong(1, memberCoupon.getMemberId());
            ps.setLong(2, memberCoupon.getCouponId());
            ps.setDate(3, Date.valueOf(memberCoupon.getExpiredDate()));
        }));
    }
}
