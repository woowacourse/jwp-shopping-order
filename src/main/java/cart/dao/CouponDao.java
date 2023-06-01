package cart.dao;


import cart.entity.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
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


    public Boolean checkCouponById(Long id) {
        String sql = "select exists(select * from coupon where id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public List<CouponEntity> findAllCoupons() {
        String sql = "select * from coupon";

        return jdbcTemplate.query(sql, rowMapper);
    }
}
