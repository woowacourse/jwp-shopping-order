package cart.config;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;

    @BeforeEach
    void repositorySetUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
    }
}
