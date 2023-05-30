package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
