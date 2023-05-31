package cart.dao;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderItemDao {

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final NamedParameterJdbcOperations jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void saveAll(final List<OrderItemEntity> entities) {
        final SqlParameterSource[] batchArgs = entities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(batchArgs);
    }
}
