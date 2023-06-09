package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Coupon> rowMapper = (resultSet, rowNum) -> new Coupon(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            CouponType.from(resultSet.getString("type")),
            resultSet.getInt("amount")
    );

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingColumns("name", "type", "amount")
                .usingGeneratedKeyColumns("id");
    }

    public Coupon save(Coupon couponToSave) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(couponToSave);
        long savedId = this.simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Coupon(
                savedId,
                couponToSave.getName(),
                CouponType.from(couponToSave.getType()),
                couponToSave.getAmount()
        );
    }

    public Coupon findById(long couponId) {
        String sql = "SELECT id, name, type, amount FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{couponId}, rowMapper);
    }

    public List<Coupon> findAll() {
        String sql = "SELECT id, name, type, amount FROM coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT id, name, type, amount FROM coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
