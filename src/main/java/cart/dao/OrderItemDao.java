package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
