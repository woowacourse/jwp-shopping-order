package cart.dao;

import cart.entity.CouponEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) ->
            new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("discount_type"),
                    rs.getInt("minimum_price"),
                    rs.getInt("discount_price"),
                    rs.getDouble("discount_rate")
            );

    public Optional<CouponEntity> findAvailableCouponByMember(Long memberId, Long couponId) {
        try {
            String sql = "SELECT * " +
                    "FROM member_coupon " +
                    "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                    "WHERE member_coupon.member_id = ? and member_coupon.coupon_id = ? and availability = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, memberId, couponId, true));
        } catch (DataAccessException e){
            return Optional.empty();
        }
    }

    public void changeUserUsedCouponAvailability(Long couponId) {
        String sql = "UPDATE member_coupon SET availability = ? WHERE coupon_id = ? and availability = ?";

        jdbcTemplate.update(sql, false, couponId, false);
    }
}
