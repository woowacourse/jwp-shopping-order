package cart.fixture;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public class RepositoryFixture {

    public static ProductRepository productRepository(JdbcTemplate jdbcTemplate) {
        ProductDao productDao = new ProductDao(jdbcTemplate);

        return new ProductRepository(productDao);
    }

    public static CartItemRepository cartItemRepository(JdbcTemplate jdbcTemplate) {
        CartItemDao cartItemDao = new CartItemDao(jdbcTemplate);

        return new CartItemRepository(cartItemDao);
    }

    public static MemberRepository memberRepository(JdbcTemplate jdbcTemplate) {
        MemberDao memberDao = new MemberDao(jdbcTemplate);

        return new MemberRepository(memberDao);
    }
}
