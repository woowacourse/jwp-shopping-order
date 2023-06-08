package cart.db.dao;

import cart.db.entity.MemberCouponEntity;
import cart.exception.CouponException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberCouponDao {

    private static final String SELECT_MEMBER_COUPON_SQL = "SELECT id, is_used, member_id, coupon_id FROM member_coupon ";
    private static final String WHERE_MEMBER_ID = "WHERE member_id = ? ";
    private static final String WHERE_MEMBER_COUPON_ID = "WHERE id = ? ";
    private static final String WHERE_IN_IDS = "WHERE id IN (:ids) ";
    private static final String FOR_UPDATE = "FOR UPDATE ";

    private static final RowMapper<MemberCouponEntity> MEMBER_COUPON_ROW_MAPPER = (rs, rowNum) ->
            new MemberCouponEntity(
                    rs.getLong("id"),
                    rs.getBoolean("is_used"),
                    rs.getLong("member_id"),
                    rs.getLong("coupon_id")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(MemberCouponEntity memberCoupon) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("is_used", false)
                .addValue("member_id", memberCoupon.getMemberId())
                .addValue("coupon_id", memberCoupon.getCouponId());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public List<MemberCouponEntity> findAllByMemberId(Long memberId) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_MEMBER_ID;
        return jdbcTemplate.query(sql, MEMBER_COUPON_ROW_MAPPER, memberId);
    }

    public MemberCouponEntity findByIdForUpdate(Long id) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_MEMBER_COUPON_ID + FOR_UPDATE;
        try {
            return jdbcTemplate.queryForObject(sql, MEMBER_COUPON_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new CouponException.NotFound();
        }
    }

    public List<MemberCouponEntity> findAllByIdForUpdate(List<Long> memberCouponIds) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_IN_IDS + FOR_UPDATE;
        SqlParameterSource sources = new MapSqlParameterSource("ids", memberCouponIds);
        return namedParameterJdbcTemplate.query(sql, sources, MEMBER_COUPON_ROW_MAPPER);
    }

    public void updateCouponStatus(MemberCouponEntity memberCoupon) {
        String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ? ";
        jdbcTemplate.update(sql, memberCoupon.getUsed(), memberCoupon.getId());
    }

    public List<MemberCouponEntity> findAllById(List<Long> memberCouponIds) {
        String sql = SELECT_MEMBER_COUPON_SQL + WHERE_IN_IDS;
        SqlParameterSource sources = new MapSqlParameterSource("ids", memberCouponIds);
        return namedParameterJdbcTemplate.query(sql, sources, MEMBER_COUPON_ROW_MAPPER);
    }
}
