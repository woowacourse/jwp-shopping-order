package cart.dao.cart;

import cart.dao.member.MemberDao;
import cart.dao.product.ProductDao;
import cart.entity.CartItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.ako;
import static cart.fixture.ProductFixture.chicken;
import static cart.fixture.ProductFixture.fork;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
@Import({ProductDao.class, MemberDao.class, CartItemDao.class})
class CartItemDaoTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;

    @Test
    @DisplayName("여러 cart-item의 id가 주어졌을 때 cartItem 리스트를 반환한다.")
    void find_by_ids() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);


        CartItemEntity akoChicken = new CartItemEntity(3, akoId, chickenId);
        CartItemEntity akoFork = new CartItemEntity(2, akoId, forkId);
        Long akoChickenId = cartItemDao.save(akoChicken);
        Long akoForkId = cartItemDao.save(akoFork);

        List<Long> cartItemIds = List.of(akoChickenId, akoForkId);

        List<CartItemEntity> expect = List.of(akoChicken, akoFork);

        // when
        List<CartItemEntity> result = cartItemDao.findByIds(cartItemIds);

        // then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expect);

    }

    @Test
    @DisplayName("cartItemId의 리스트가 요청오면 모두 삭제한다.")
    void delete_cart_item_by_ids() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        CartItemEntity akoChicken = new CartItemEntity(10, akoId, chickenId);
        CartItemEntity akoFork = new CartItemEntity(5, akoId, forkId);
        Long akoChickenId = cartItemDao.save(akoChicken);
        Long akoForkId = cartItemDao.save(akoFork);

        List<Long> cartItemIds = List.of(akoChickenId, akoForkId);

        // when
        cartItemDao.deleteByIds(cartItemIds);
        List<CartItemEntity> result = cartItemDao.findByMemberId(akoId);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("상품 수량을 업데이트한다.")
    void update_cart_quantity() {
        // given
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        CartItemEntity akoFork = new CartItemEntity(5, akoId, forkId);
        Long akoForkId = cartItemDao.save(akoFork);

        CartItemEntity updateAkoFork = new CartItemEntity(akoForkId, 10, akoId, forkId);

        // when
        cartItemDao.updateQuantity(updateAkoFork);
        CartItemEntity result = cartItemDao.findById(akoForkId).get();

        // then
        assertThat(result.getQuantity()).isEqualTo(updateAkoFork.getQuantity());
    }

    @Test
    @DisplayName("장바구니 상품을 id로 삭제한다.")
    void delete_cart_item_by_id() {
        // given
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        CartItemEntity akoFork = new CartItemEntity(5, akoId, forkId);
        Long akoForkId = cartItemDao.save(akoFork);

        // when
        cartItemDao.deleteById(akoForkId);
        Optional<CartItemEntity> result = cartItemDao.findById(akoForkId);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("장바구니 상품을 상품id와 사용자 id로 삭제한다.")
    void delete_cart_item_by_product_id_and_member_id() {
        // given
        Long forkId = productDao.createProduct(fork);
        Long akoId = memberDao.addMember(ako);

        CartItemEntity akoFork = new CartItemEntity(5, akoId, forkId);
        Long akoForkId = cartItemDao.save(akoFork);

        // when
        cartItemDao.delete(akoId, forkId);
        Optional<CartItemEntity> result = cartItemDao.findById(akoForkId);

        // then
        assertThat(result.isPresent()).isFalse();
    }

}
