package cart.dao;

import cart.dao.dto.MemberCouponDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private static final RowMapper<MemberCouponDto> MEMBER_COUPON_DTO_ROW_MAPPER = (rs, rn) ->
            new MemberCouponDto(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("coupon_id")
            );

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberCouponDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<MemberCouponDto> findByMemberId(final Long memberId) {
        final String sql = "select * from member_coupon where member_id = :memberId AND used = false";
        return jdbcTemplate.query(sql, Map.of("memberId", memberId), MEMBER_COUPON_DTO_ROW_MAPPER);
    }

    public Optional<MemberCouponDto> findUnUsedCouponById(final Long id) {
        final String sql = "select * from member_coupon where id = :id AND used = false";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), MEMBER_COUPON_DTO_ROW_MAPPER));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<MemberCouponDto> findUsedCouponById(final Long id) {
        final String sql = "select * from member_coupon where id = :id AND used = true";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), MEMBER_COUPON_DTO_ROW_MAPPER));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Long insert(final MemberCouponDto memberCouponDto) {
        return jdbcInsert.executeAndReturnKey(
                        Map.of("member_id", memberCouponDto.getMemberId()
                                , "coupon_id", memberCouponDto.getCouponId()
                                , "used", false)
                )
                .longValue();
    }

    public void updateUsedTrue(final Long id) {
        final String sql = "update member_coupon set used = true where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
