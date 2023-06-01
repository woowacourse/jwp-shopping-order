package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class OrderDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName("orders");
    }

    public Long insert(OrderEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }
}
