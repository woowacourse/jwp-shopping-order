package coupon.db.dao;

import coupon.domain.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CouponQueryDao {

    private static final RowMapper<Coupon> rowMapper = (rs, rowNum) -> new Coupon(
            rs.getLong("c_id"),
            rs.getString("c_name"),
            DiscountConditionHelper.findByName(rs.getString("dc_discount_condition")).mapRow(rs, rowNum),
            DiscountPolicyHelper.findByName(rs.getString("dt_discount_type")).mapRow(rs, rowNum)
    );
    private static final String FIND_BY_MEMBER_ID_SQL =
            "SELECT c.id as c_id, c.name as c_name, c.discount_condition_id as c_discount_condition_id, c.discount_type_id as c_discount_type_id, "
                    + "dc.discount_condition_type as dc_discount_condition,"
                    + "dt.discount_type as dt_discount_type, dt.discount_amount_id as dt_discount_amount,"
                    + "ad.rate as ad_rate "
                    + "FROM COUPON as c "
                    + "left join DISCOUNT_CONDITION as dc on c.discount_condition_id=dc.id "
                    + "left join DISCOUNT_TYPE as dt on dt.id=c.discount_type_id "
                    + "left join AMOUNT_DISCOUNT as ad on ad.id=dt.discount_amount_id "
                    + "join COUPON_MEMBER as mc on mc.coupon_id=c.id "
                    + "where mc.member_id=?";
    private static final String FIND_BY_ID_SQL =
            "SELECT c.id as c_id, c.name as c_name, c.discount_condition_id as c_discount_condition_id, c.discount_type_id as c_discount_type_id, "
                    + "       dc.discount_condition_type as dc_discount_condition, "
                    + "       dt.discount_type as dt_discount_type, dt.discount_amount_id as dt_discount_amount, "
                    + "       ad.rate as ad_rate "
                    + "FROM COUPON as c "
                    + "LEFT JOIN DISCOUNT_CONDITION as dc ON c.discount_condition_id = dc.id "
                    + "LEFT JOIN DISCOUNT_TYPE as dt ON dt.id = c.discount_type_id "
                    + "LEFT JOIN AMOUNT_DISCOUNT as ad ON ad.id = dt.discount_amount_id "
                    + "WHERE c.id = ?";


    private final JdbcTemplate jdbcTemplate;

    public CouponQueryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Coupon> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        return jdbcTemplate.query(FIND_BY_MEMBER_ID_SQL, rowMapper, memberId);
    }
}
