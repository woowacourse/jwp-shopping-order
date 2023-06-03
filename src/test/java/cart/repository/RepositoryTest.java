package cart.repository;

import cart.dao.*;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@JdbcTest
public class RepositoryTest {
    protected final CartItemDao cartItemDao;
    protected final CouponDao couponDao;
    protected final MemberCouponDao memberCouponDao;
    protected final MemberDao memberDao;
    protected final OrdersCartItemDao ordersCartItemDao;
    protected final OrdersCouponDao ordersCouponDao;
    protected final OrdersDao ordersDao;
    protected final ProductDao productDao;

    protected RepositoryTest(JdbcTemplate jdbcTemplate) {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.couponDao = new CouponDao(jdbcTemplate);
        this.memberCouponDao = new MemberCouponDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.ordersCartItemDao = new OrdersCartItemDao(jdbcTemplate);
        this.ordersCouponDao = new OrdersCouponDao(jdbcTemplate);
        this.ordersDao = new OrdersDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
    }
}
