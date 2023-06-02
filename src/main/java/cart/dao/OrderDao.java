package cart.dao;

import cart.entity.OrderEntity;
import cart.exception.OrderNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static cart.entity.RowMapperUtil.orderEntityRowMapper;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingColumns("member_id", "original_price", "discount_price")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(final OrderEntity source) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(source);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<OrderEntity> findByMemberId(final long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderEntityRowMapper, memberId);
    }

    public OrderEntity findById(final long id) {
        final String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, orderEntityRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new OrderNotFoundException();
        }
    }
}
