package cart.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.dao.dto.MemberCouponDto;
import cart.domain.MemberCoupon;

@Repository
public class MemberCouponDao {

    private static final RowMapper<MemberCouponDto> MEMBER_COUPON_DTO_ROW_MAPPER = (rs, rowNum) -> new MemberCouponDto(
            rs.getLong("id"),
            rs.getLong("owner_id"),
            rs.getLong("coupon_id"),
            rs.getBoolean("is_used")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(MemberCoupon memberCoupon) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(MemberCouponDto.of(memberCoupon));

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public MemberCouponDto selectBy(Long id) {
        String sql = "select * from member_coupon where id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                MEMBER_COUPON_DTO_ROW_MAPPER,
                id
        );
    }

    public void update(MemberCoupon memberCoupon) {
        String sql = "update member_coupon set is_used = ? where id = ?";

        jdbcTemplate.update(
                sql,
                memberCoupon.isUsed(),
                memberCoupon.getId()
        );
    }

    public List<MemberCouponDto> selectAllBy(Long ownerId) {
        String sql = "select * from member_coupon where owner_id = ? and is_used = false";

        return jdbcTemplate.query(
                sql,
                MEMBER_COUPON_DTO_ROW_MAPPER,
                ownerId
        );
    }
}
