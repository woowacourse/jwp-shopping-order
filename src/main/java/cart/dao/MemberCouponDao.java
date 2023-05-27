package cart.dao;

import cart.entity.MemberCouponEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<MemberCouponEntity> rowMapper = (resultSet, rowNum) -> new MemberCouponEntity(
            resultSet.getLong("id"),
            resultSet.getLong("coupon_id"),
            resultSet.getLong("member_id"),
            resultSet.getBoolean("used")
    );

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    public MemberCouponEntity insert(final MemberCouponEntity memberCouponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(memberCouponEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new MemberCouponEntity(
                id,
                memberCouponEntity.getCouponId(),
                memberCouponEntity.getMemberId(),
                memberCouponEntity.isUsed()
        );
    }

    public List<MemberCouponEntity> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM member_coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
