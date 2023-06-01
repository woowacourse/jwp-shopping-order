package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("cart_order")
                .usingGeneratedKeyColumns("id");
    }

    private static RowMapper<Long> rowMapper() {
        return (rs, rowNum) -> rs.getLong("id");
    }

    public Long create(Long memberId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Long> findAllByMemberId(Long memberId) {
        String sql = "select * from cart_order where member_id = ?";

        return jdbcTemplate.query(sql, rowMapper(), memberId);
    }

    public Long findAllById(Long id) {
        String sql = "select * from cart_order where id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }
}
