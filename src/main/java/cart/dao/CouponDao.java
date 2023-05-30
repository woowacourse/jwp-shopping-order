package cart.dao;

import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeCouponEntity;
import cart.dao.entity.CouponTypeEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class CouponDao {

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<CouponTypeCouponEntity> couponTypeCouponEntityRowMapper = (rs, num) ->
            new CouponTypeCouponEntity(
                    rs.getLong("couponTypeId"),
                    rs.getLong("couponId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("discountAmount"),
                    rs.getBoolean("usageStatus")
    );
    private final RowMapper<CouponTypeEntity> couponTypeEntityRowMapper = (rs, num) ->
            new CouponTypeEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("discount_amount")
            );

    public CouponDao(final NamedParameterJdbcOperations jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long issue(final CouponEntity couponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(couponEntity);
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<CouponTypeCouponEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT c.id couponId, " +
                "ct.id couponTypeId, " +
                "ct.name name, " +
                "ct.description description, " +
                "ct.discount_amount discountAmount, " +
                "c.usage_status usageStatus " +
                "FROM coupon c " +
                "JOIN coupon_type ct ON c.coupon_type_id = ct.id " +
                "WHERE c.member_id = :memberId";

        final Map<String, Long> params = Collections.singletonMap("memberId", memberId);

        return jdbcTemplate.query(sql, params, couponTypeCouponEntityRowMapper);
    }

    public void changeStatus(final Long couponId, final Long memberId) {
        final String sql = "UPDATE coupon SET usage_status = true " +
                "AND id = :couponId AND member_id = :memberId";

        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("couponId", couponId)
                .addValue("memberId", memberId);

        jdbcTemplate.update(sql, params);
    }

    public List<CouponTypeEntity> findAll() {
        final String sql = "SELECT id, name, description, discount_amount FROM coupon_type";

        return jdbcTemplate.query(sql, couponTypeEntityRowMapper);
    }
}
