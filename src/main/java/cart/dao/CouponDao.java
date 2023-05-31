package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<Coupon> couponRowMapper = (rs, rowNum) -> new Coupon(
            rs.getLong("id"),
            rs.getString("name"),
            CouponType.from(rs.getString("type")),
            rs.getInt("discount_amount")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Coupon coupon) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(coupon);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<Coupon> findById(Long id) {
        String sql = "select * from coupon where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, couponRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Coupon> findAll() {
        String sql = "select * from coupon";
        return jdbcTemplate.query(sql, couponRowMapper);
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        String sql = "select coupon.id, coupon.name, coupon.type, coupon.discount_amount, member_coupon.member_id " +
                "from coupon " +
                "join member_coupon on coupon.id = member_coupon.coupon_id " +
                "where member_coupon.member_id = ?";
        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }
}
