package common;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@AutoConfigureJdbc
public class DatabaseConfiguration {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public MemberDao memberDao() {
        return new MemberDao(jdbcTemplate);
    }

    @Bean
    public ProductDao productDao() {
        return new ProductDao(jdbcTemplate);
    }

    @Bean
    public CartItemDao cartItemDao() {
        return new CartItemDao(jdbcTemplate);
    }
}
