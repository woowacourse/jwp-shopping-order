package cart.config;

import cart.cartitem.dao.CartItemDao;
import cart.member.dao.MemberDao;
import cart.order.dao.OrderHistoryDao;
import cart.order.dao.OrderItemDao;
import cart.product.dao.ProductDao;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/reset-data.sql")
@Import({CartItemDao.class,
        MemberDao.class,
        ProductDao.class,
        OrderHistoryDao.class,
        OrderItemDao.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DaoTest {

    @Autowired
    protected CartItemDao cartItemDao;

    @Autowired
    protected MemberDao memberDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected OrderHistoryDao orderHistoryDao;

    @Autowired
    protected OrderItemDao orderItemDao;
}
