package cart.dao;

import cart.dao.dto.MemberCouponDto;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private static final RowMapper<MemberCouponDto> MEMBER_COUPON_DTO_ROW_MAPPER = (rs, rn) ->
            new MemberCouponDto(rs.getLong("id"), rs.getLong("member_id"), rs.getLong("coupon_id"));
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberCouponDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<MemberCouponDto> findByMemberId(final Long memberId) {
        final String sql = "select * from member_coupon where member_id = :memberId";
        return jdbcTemplate.query(sql, Map.of("memberId", memberId), MEMBER_COUPON_DTO_ROW_MAPPER
        );
    }

    public MemberCouponDto findById(final Long id) {
        final String sql = "select * from member_coupon where id = :id";
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), MEMBER_COUPON_DTO_ROW_MAPPER);
    }

    public Long insert(final Long memberId, final Long couponId) {
        return jdbcInsert.executeAndReturnKey(Map.of("member_id", memberId, "coupon_id", couponId)).longValue();
    }
}
