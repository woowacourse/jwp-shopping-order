package cart.dao;

import cart.dao.entity.MemberCouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("member_id", "coupon_id", "is_used", "expired_at")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) -> new MemberCouponEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("coupon_id"),
            new Date(rs.getDate("expired_at").getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    );

    public List<MemberCouponEntity> findUsableByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM member_coupon " +
                "WHERE is_used = false " +
                "AND expired_at > ?" +
                "AND member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, LocalDateTime.now(), memberId);
    }

    public Optional<MemberCouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM member_coupon WHERE id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    //예외 컨트롤
    public void updateUsedById(final Long id) {
        final String sql = "UPDATE member_coupon SET is_used = ? WHERE id = ?";
        jdbcTemplate.update(sql, true, id);
    }

    public List<MemberCouponEntity> findByIds(final List<Long> ids) {
        final String sql = "SELECT * FROM member_coupon WHERE id IN (%s) ";

        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                String.format(sql, inSql),
                ids.toArray(),
                rowMapper
        );
    }

    public Long save(final MemberCouponEntity memberCouponEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("member_id", memberCouponEntity.getMemberId())
                .addValue("coupon_id", memberCouponEntity.getCouponId())
                .addValue("is_used", false)
                .addValue("expired_at", memberCouponEntity.getExpiredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ).longValue();
    }
}
