package cart.config;

import cart.dao.*;
import cart.fixture.dao.*;
import org.junit.jupiter.api.BeforeEach;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;
    protected OrderItemDao orderItemDao;
    protected OrderDao orderDao;

    protected MemberDaoFixture memberDaoFixture;
    protected ProductDaoFixture productDaoFixture;
    protected CartItemDaoFixture cartItemDaoFixture;
    protected OrderItemDaoFixture orderItemDaoFixture;
    protected OrderDaoFixture orderDaoFixture;

    @BeforeEach
    void repositorySetUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);

        memberDaoFixture = new MemberDaoFixture(memberDao);
        productDaoFixture = new ProductDaoFixture(productDao);
        cartItemDaoFixture = new CartItemDaoFixture(cartItemDao);
        orderItemDaoFixture = new OrderItemDaoFixture(orderItemDao);
        orderDaoFixture = new OrderDaoFixture(orderDao);
    }
}
