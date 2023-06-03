package cart.coupon.application;

import cart.coupon.Coupon;
import cart.discountpolicy.application.DiscountPolicyRepository;
import cart.discountpolicy.discountcondition.DiscountTarget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao implements CouponRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final DiscountPolicyRepository discountPolicyRepository;

    public CouponDao(DataSource dataSource, DiscountPolicyRepository discountPolicyRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.discountPolicyRepository = discountPolicyRepository;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(String name, Long discountPolicyId) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", name);
        parameterSource.addValue("discount_policy_id", discountPolicyId);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    @Override
    public Coupon findById(Long id) {
        final var sql = "select * from COUPON where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                this.discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ), id);
    }

    @Override
    public List<Coupon> findAllByMemberId(Long memberId) {
        final var sql = "select * from COUPON C " +
                "inner join MEMBER_COUPONS MC on MC.coupon_id = C.id " +
                "where MC.member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                this.discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ), memberId);
    }

    @Override
    public void giveCouponToMember(Long memberId, Long couponId) {
        final var sql = "insert into MEMBER_COUPONS (member_id, coupon_id) values (?, ?)";
        jdbcTemplate.update(sql, memberId, couponId);
    }

    @Override
    public List<Coupon> findAllByMemberIdExcludingTarget(Long memberId, List<DiscountTarget> discountTargets) {
        final var sql = "select * from COUPON C " +
                "inner join MEMBER_COUPONS MC on MC.coupon_id = c.id " +
                "inner join DISCOUNT_POLICY DP on C.discount_policy_id = DP.id " +
                "where MC.member_id = ? and DP.target != TOTAL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                this.discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ));
    }

    @Override
    public List<Coupon> findAllByMemberIdApplyingToTotalPrice(Long memberId) {
        final var sql = "select * from COUPON C " +
                "inner join MEMBER_COUPONS MC on MC.coupon_id = c.id " +
                "inner join DISCOUNT_POLICY DP on C.discount_policy_id = DP.id " +
                "where MC.member_id = ? and DP.target = TOTAL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                this.discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ));
    }
}
