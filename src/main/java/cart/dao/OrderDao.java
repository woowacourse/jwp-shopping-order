package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("cart_order")
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
}
