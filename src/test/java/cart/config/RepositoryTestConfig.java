package cart.config;

import cart.dao.*;
import org.junit.jupiter.api.BeforeEach;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;
    protected OrderItemDao orderItemDao;
    protected OrderDao orderDao;

    @BeforeEach
    void repositorySetUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }
}
