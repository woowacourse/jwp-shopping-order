package cart.repository;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberCouponRepository {

    private static final String SELECT_MEMBER_COUPON_SQL = "SELECT member_coupon.id, member_coupon.is_used, member.id, coupon.id, coupon.coupon_name, coupon.discount_type, coupon.discount_value " +
            "FROM member_coupon " +
            "INNER JOIN member ON member_coupon.coupon_id = coupon.id " +
            "INNER JOIN coupon ON member_coupon.member_id = member.id ";
    private static final String WHERE_MEMBER_ID = "WHERE member_coupon.member_id = ? ";
    private static final String WHERE_MEMBER_COUPON_ID = "WHERE member_coupon.id = ? ";
    private static final String FOR_UPDATE = "FOR UPDATE ";

    private final DiscountPolicyProvider discountPolicyProvider;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberCouponRepository(DiscountPolicyProvider discountPolicyProvider, JdbcTemplate jdbcTemplate) {
        this.discountPolicyProvider = discountPolicyProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Member member, Coupon coupon) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("is_used", false)
                .addValue("member_id", member.getId())
                .addValue("coupon_id", coupon.getId());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public List<MemberCoupon> findAllByMemberId(Long memberId) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_MEMBER_ID;
        return jdbcTemplate.query(sql, getMemberCouponRowMapper(), memberId);
    }

    private RowMapper<MemberCoupon> getMemberCouponRowMapper() {
        return (rs, rowNum) -> {
            String typeName = rs.getString("discount_type");
            DiscountPolicy discountPolicy = discountPolicyProvider.getByType(DiscountType.valueOf(typeName));
            Coupon coupon = new Coupon(
                    rs.getLong("coupon.id"),
                    rs.getString("coupon_name"),
                    discountPolicy,
                    rs.getDouble("discount_value")
            );
            return new MemberCoupon(
                    rs.getLong("member_coupon.id"),
                    coupon,
                    rs.getBoolean("is_used")
            );
        };
    }

    public MemberCoupon findByIdForUpdate(Long id) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_MEMBER_COUPON_ID + FOR_UPDATE;
        return jdbcTemplate.queryForObject(sql, getMemberCouponRowMapper(), id);
    }

    public void updateCouponStatus(MemberCoupon memberCoupon) {
        String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ? ";
        jdbcTemplate.update(sql, memberCoupon.isUsed(), memberCoupon.getId());
    }
}
