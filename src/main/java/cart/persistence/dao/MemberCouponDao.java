package cart.persistence.dao;

import cart.persistence.MemberCouponDetail;
import cart.persistence.entity.MemberCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<MemberCouponDetail> rowMapper =
            (rs, rowNum) -> new MemberCouponDetail(
                    rs.getLong("member_coupon.id"),
                    rs.getLong("member.id"),
                    rs.getString("member.name"),
                    rs.getString("password"),
                    rs.getLong("coupon.id"),
                    rs.getInt("period"),
                    rs.getString("coupon.name"),
                    rs.getInt("discount_rate"),
                    rs.getTimestamp("issued_at").toLocalDateTime(),
                    rs.getTimestamp("coupon.expired_at").toLocalDateTime(),
                    rs.getTimestamp("member_coupon.expired_at").toLocalDateTime(),
                    rs.getBoolean("member_coupon.is_used")
            );


    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(MemberCouponEntity memberCouponEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(memberCouponEntity);

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    public List<MemberCouponDetail> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM member_coupon " +
                "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                "INNER JOIN member ON member_coupon.member_id = member.id " +
                "WHERE member_coupon.member_id = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public int updateCouponUseStatus(Long id, Boolean isUsed) {
        String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ?";

        return jdbcTemplate.update(sql, isUsed, id);
    }

}
