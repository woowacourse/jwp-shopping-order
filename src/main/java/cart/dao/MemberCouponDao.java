package cart.dao;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                    rs.getLong("member_coupon.id"),
                    rs.getString("name"),
                    rs.getString("discount_type"),
                    rs.getInt("minimum_price"),
                    rs.getInt("discount_price"),
                    rs.getDouble("discount_rate")
            );

    public Optional<CouponEntity> findByIdAndMemberIdAndAvailable(Long couponId, Long memberId, Boolean available) {
        try {
            String sql = "SELECT * " +
                    "FROM member_coupon " +
                    "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                    "WHERE member_coupon.member_id = ? and member_coupon.id = ? and available = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, memberId, couponId, available));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateCouponAvailabilityByIdAndAvailable(Long memberCouponId, Boolean available) {
        String sql = "UPDATE member_coupon SET available = ? WHERE id = ? and available = ?";

        jdbcTemplate.update(sql, !available, memberCouponId, available);
    }

    public boolean existsByCouponIdAndMemberId(Long couponId, Long memberId) {
        String sql = "select exists(select * from member_coupon where coupon_id = ? and available = ? and member_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, couponId, true, memberId);
    }

    public Long save(MemberCouponEntity memberCouponEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(memberCouponEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<CouponEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * " +
                "FROM member_coupon " +
                "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                " where member_id = ? and available = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId, true);
    }


    public Optional<CouponEntity> findById(Long savedId) {
        try {
            String sql = "SELECT * " +
                    "FROM member_coupon " +
                    "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                    " where member_coupon.id = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, savedId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
