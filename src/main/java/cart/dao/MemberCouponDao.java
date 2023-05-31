package cart.dao;

import cart.entity.MemberCouponEntity;
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
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<MemberCouponEntity> rowMapper = (resultSet, rowNum) -> new MemberCouponEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("coupon_id"),
            resultSet.getBoolean("used")
    );

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("member_id", "coupon_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    public MemberCouponEntity insert(final MemberCouponEntity memberCouponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(memberCouponEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new MemberCouponEntity(
                id,
                memberCouponEntity.getMemberId(),
                memberCouponEntity.getCouponId(),
                memberCouponEntity.isUsed()
        );
    }

    public void insertAll(final List<MemberCouponEntity> memberCouponEntities) {
        final BeanPropertySqlParameterSource[] parameterSources = memberCouponEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        jdbcInsert.executeBatch(parameterSources);
    }

    public List<MemberCouponEntity> findAllByUsedAndMemberId(final boolean used, final Long memberId) {
        final String sql = "SELECT * FROM member_coupon WHERE used = ? and member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, used, memberId);
    }

    public void update(final MemberCouponEntity memberCouponEntity) {
        final String sql = "UPDATE member_coupon SET used = ? where coupon_id = ? and member_id = ?";
        jdbcTemplate.update(
                sql,
                memberCouponEntity.isUsed(),
                memberCouponEntity.getCouponId(),
                memberCouponEntity.getMemberId()
        );
    }

    public Optional<MemberCouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM member_coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
