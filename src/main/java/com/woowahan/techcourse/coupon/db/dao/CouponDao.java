package com.woowahan.techcourse.coupon.db.dao;

import com.woowahan.techcourse.coupon.domain.Coupon;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<Coupon> rowMapper = (rs, rowNum) -> new Coupon(
            rs.getLong("c_id"),
            rs.getString("c_name"),
            DiscountConditionHelper.findByName(rs.getString("dc_discount_condition")).mapRow(rs, rowNum),
            DiscountPolicyHelper.findByName(rs.getString("dt_discount_type")).mapRow(rs, rowNum)
    );
    private static final String BASE_FIND_ALL_SQL =
            "SELECT c.id AS c_id, c.name AS c_name, c.discount_condition_id AS c_discount_condition_id, c.discount_type_id AS c_discount_type_id, "
                    + "dc.discount_condition_type AS dc_discount_condition,"
                    + "dt.discount_type AS dt_discount_type, dt.discount_amount_id AS dt_discount_amount,"
                    + "ad.rate AS ad_rate "
                    + "FROM coupon AS c "
                    + "LEFT JOIN discount_condition AS dc ON c.discount_condition_id=dc.id "
                    + "LEFT JOIN discount_type AS dt ON dt.id=c.discount_type_id "
                    + "LEFT JOIN amount_discount AS ad ON ad.id=dt.discount_amount_id ";
    private static final String FIND_BY_MEMBER_ID_SQL =
            BASE_FIND_ALL_SQL + "JOIN coupon_member AS mc ON mc.coupon_id=c.id WHERE mc.member_id=?";
    private static final String FIND_BY_ID_SQL =
            BASE_FIND_ALL_SQL + "WHERE c.id=?";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Optional<Coupon> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        return jdbcTemplate.query(FIND_BY_MEMBER_ID_SQL, rowMapper, memberId);
    }

    public List<Coupon> findAll() {
        return jdbcTemplate.query(BASE_FIND_ALL_SQL, rowMapper);
    }

    public List<Coupon> findAllByIds(@NotNull List<Long> couponIds) {
        if (isEmpty(couponIds)) {
            return List.of();
        }
        return executeFindAllByIds(couponIds);
    }

    private boolean isEmpty(List<Long> couponIds) {
        return couponIds == null || couponIds.isEmpty();
    }

    private List<Coupon> executeFindAllByIds(List<Long> couponIds) {
        String sql = BASE_FIND_ALL_SQL + " WHERE c.id IN (:couponIds)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("couponIds", couponIds);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
    }
}
