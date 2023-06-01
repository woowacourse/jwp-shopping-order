package cart.persistence.dao;

import cart.persistence.entity.MemberCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("member_id", "coupon_id")
                .usingGeneratedKeyColumns("id");
    }

    public List<MemberCouponEntity> findAll() {
        String sql = "SELECT * from member_coupon";
        return jdbcTemplate.query(sql, new MemberCouponEntityRowMapper());
    }

    public List<MemberCouponEntity> findMemberCouponsByMemberId(Long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new MemberCouponEntityRowMapper());
    }

    public Optional<MemberCouponEntity> findById(Long id) {
        String sql = "SELECT * FROM member_coupon WHERE id = ?";
        List<MemberCouponEntity> memberCoupons = jdbcTemplate.query(sql, new Object[]{id}, new MemberCouponEntityRowMapper());
        return memberCoupons.isEmpty() ? Optional.empty() : Optional.ofNullable(memberCoupons.get(0));
    }

    public Optional<MemberCouponEntity> findOneByCouponId(Long couponId) {
        String sql = "SELECT * FROM member_coupon WHERE coupon_id = ?";
        List<MemberCouponEntity> memberCoupons = jdbcTemplate.query(sql, new Object[]{couponId}, new MemberCouponEntityRowMapper());
        return memberCoupons.isEmpty() ? Optional.empty() : Optional.ofNullable(memberCoupons.get(0));
    }

    public Long add(MemberCouponEntity memberCouponEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberCouponEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM member_coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class MemberCouponEntityRowMapper implements RowMapper<MemberCouponEntity> {
        @Override
        public MemberCouponEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberCouponEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("coupon_id")
            );
        }
    }
}
