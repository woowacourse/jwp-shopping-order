package cart.dao;

import cart.entity.CouponIssueConditionEntity;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CouponIssueConditionDao {

    private static final RowMapper<CouponIssueConditionEntity> ROW_MAPPER = (rs, rowNum) -> {
        long id = rs.getLong("id");
        long couponId = rs.getLong("coupon_id");
        BigDecimal minIssuePrice = rs.getBigDecimal("min_issue_price");
        return new CouponIssueConditionEntity(id, couponId, minIssuePrice);
    };

    private final JdbcTemplate jdbcTemplate;

    public CouponIssueConditionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CouponIssueConditionEntity> findAll() {
        String sql = "SELECT * FROM coupon_issue_condition";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Long save(CouponIssueConditionEntity couponIssueCondition) {
        String sql = "INSERT INTO coupon_issue_condition (coupon_id, min_issue_price) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, couponIssueCondition.getCouponId());
            ps.setBigDecimal(2, couponIssueCondition.getMinIssuePrice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
