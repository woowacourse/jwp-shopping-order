package cart.persistence.dao;

import cart.persistence.dto.MemberCouponDetailDTO;
import cart.persistence.entity.MemberCouponEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public long create(final MemberCouponEntity memberCoupon) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO member_coupon (member_id, coupon_id, is_used, expired_at) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, memberCoupon.getMemberId());
            ps.setLong(2, memberCoupon.getCouponId());
            ps.setBoolean(3, memberCoupon.getIsUsed());
            ps.setTimestamp(4, memberCoupon.getExpiredAt());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<MemberCouponDetailDTO> findById(final long id) {
        String sql = "SELECT * FROM member_coupon "
                + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id "
                + "INNER JOIN member ON member_coupon.member_id = member.id "
                + "WHERE member_coupon.id = ? ";
        try {
            MemberCouponDetailDTO memberCoupon = jdbcTemplate.queryForObject(sql,
                    RowMapperHelper.memberCouponDetailRowMapper(), id);
            return Optional.of(memberCoupon);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<MemberCouponDetailDTO> findValidCouponByMemberId(final long memberId) {
        String sql = "SELECT * FROM member_coupon "
                + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id "
                + "INNER JOIN member ON member_coupon.member_id = member.id "
                + "WHERE member_id = ? "
                + "AND is_used = FALSE "
                + "AND expired_at > CURRENT_TIMESTAMP ";
        return jdbcTemplate.query(sql, RowMapperHelper.memberCouponDetailRowMapper(), memberId);
    }

    public void changeCouponStatus(final long id, final boolean isUsed) {
        String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ?";
        jdbcTemplate.update(sql, isUsed, id);
    }
}
