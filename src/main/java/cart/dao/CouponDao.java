package cart.dao;


import cart.entity.CouponEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public Optional<CouponEntity> findCouponByName(CouponEntity coupon) {
        try {
            String sql = "select * from coupon where name like ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, coupon.getName()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
