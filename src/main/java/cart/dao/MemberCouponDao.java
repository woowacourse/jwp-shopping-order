package cart.dao;

import cart.entity.MemberCouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final Long couponId = rs.getLong("coupon_id");
        final Long memberId = rs.getLong("member_id");
        final Boolean used = rs.getBoolean("used");
        return new MemberCouponEntity(id, couponId, memberId, used);
    };

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    public List<MemberCouponEntity> findAllNotUsedMemberCouponByMemberId(final Long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ? and used = false";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<MemberCouponEntity> findNotUsedCouponByMemberIdAndCouponId(final Long memberId, final Long id) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ? and id = ? and used = false";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, memberId, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateUsedCouponByMemberIdAndCouponId(final Long memberId, final Long id) {
        String sql = "UPDATE member_coupon SET used = true WHERE member_id = ? and id = ?";
        jdbcTemplate.update(sql, memberId, id);
    }
}
