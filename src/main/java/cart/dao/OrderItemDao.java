package cart.dao;

import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public OrderItemEntity insert(final OrderItemEntity source) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(source);
        final long id = insertAction.executeAndReturnKey(params).longValue();
        return OrderItemEntity.of(id, source);
    }
}
