package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.fixture.MemberFixture.ako;
import static cart.fixture.MemberFixture.generate;
import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import({ProductDao.class, MemberDao.class, CartItemDao.class})
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;

    @AfterEach
    void clean() {
        jdbcTemplate.update("DELETE FROM cart_item");
        jdbcTemplate.update("DELETE FROM member");
        jdbcTemplate.update("DELETE FROM product");
    }

    @Test
    @DisplayName("여러 cart-item의 id가 주어졌을 때 cartItem 리스트를 반환한다.")
    void find_by_ids() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        Product savedChicken = generateChicken(chickenId);
        Product savedFork = generateFork(forkId);
        Member savedAko = generate(akoId);

        CartItem akoChicken = new CartItem(savedAko, savedChicken);
        CartItem akoFork = new CartItem(savedAko, savedFork);
        Long akoChickenId = cartItemDao.save(akoChicken);
        Long akoForkId = cartItemDao.save(akoFork);

        List<Long> cartItemIds = List.of(akoChickenId, akoForkId);

        // when
        List<CartItem> result = cartItemDao.findByIds(cartItemIds);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("cartItemId의 리스트가 요청오면 모두 삭제한다.")
    void delete_cart_item_by_ids() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        Product savedChicken = generateChicken(chickenId);
        Product savedFork = generateFork(forkId);
        Member savedAko = generate(akoId);

        CartItem akoChicken = new CartItem(savedAko, savedChicken);
        CartItem akoFork = new CartItem(savedAko, savedFork);
        Long akoChickenId = cartItemDao.save(akoChicken);
        Long akoForkId = cartItemDao.save(akoFork);

        List<Long> cartItemIds = List.of(akoChickenId, akoForkId);

        // when
        cartItemDao.deleteByIds(cartItemIds);
        List<CartItem> result = cartItemDao.findByMemberId(akoId);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

}