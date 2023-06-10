package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CartItemDao cartItemDao;
    private Long productId1;
    private Long productId2;
    private Long memberId;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        productId1 = productDao.create(ProductEntity.of(1L, "망고", 1_000, "mango.png"));
        productId2 = productDao.create(ProductEntity.of(2L, "터틀", 2_000, "turtle.png"));
        memberId = memberDao.findAll().get(0).getId();
    }

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다.")
    void testCreate() {
        // given
        final CartItemEntity cartItem = CartItemEntity.of(1L, memberId, productId1, 1);

        // when
        final Long id = cartItemDao.create(cartItem);

        // then
        assertThat(cartItemDao.findById(id).isPresent()).isTrue();
    }

    @Test
    @DisplayName("memberId로 장바구니 목록을 조회할 수 있다.")
    void testFindByMemberId() {
        // given
        final CartItemEntity cartItem1 = CartItemEntity.of(1L, memberId, productId1, 1);
        final CartItemEntity cartItem2 = CartItemEntity.of(2L, memberId, productId2, 2);
        cartItemDao.create(cartItem1);
        cartItemDao.create(cartItem2);

        // when
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(cartItemEntities).hasSize(2),
                () -> assertThat(cartItemEntities)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(cartItem1, cartItem2))
        );
    }

    @Test
    @DisplayName("memberId와 productId로 장바구니 목록을 조회할 수 있다.")
    void testFindByMemberIdAndProductId() {
        // given
        final CartItemEntity cartItem1 = CartItemEntity.of(1L, memberId, productId1, 1);
        final CartItemEntity cartItem2 = CartItemEntity.of(2L, memberId, productId2, 2);
        cartItemDao.create(cartItem1);
        cartItemDao.create(cartItem2);

        // when
        final CartItemEntity cartItemEntity = cartItemDao.findByMemberIdAndProductId(memberId, productId2)
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(cartItemEntity).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(cartItem2);
    }

    @Test
    @DisplayName("장바구니에 담긴 상품 수량을 수정할 수 있다.")
    void testUpdateQuantity() {
        // given
        final CartItemEntity cartItem = CartItemEntity.of(1L, memberId, productId1, 1);
        final Long id = cartItemDao.create(cartItem);
        final CartItemEntity updatedCartItem = CartItemEntity.of(cartItem.getId(), cartItem.getMemberId(),
                cartItem.getProductId(), 2);

        // when
        cartItemDao.updateQuantity(updatedCartItem);

        // then
        final CartItemEntity result = cartItemDao.findById(id).orElseThrow(NoSuchElementException::new);
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("id로 장바구니에 담긴 상품을 삭제할 수 있다.")
    void testDelete() {
        // given
        final CartItemEntity cartItem = CartItemEntity.of(1L, memberId, productId1, 1);
        final Long id = cartItemDao.create(cartItem);

        // when
        cartItemDao.deleteById(id);

        // then
        assertThat(cartItemDao.findById(id).isPresent()).isFalse();
    }

    @Test
    @DisplayName("memberId와 productId로 장바구니에 담긴 상품을 삭제할 수 있다.")
    void testDeleteByMemberIdAndProductId() {
        // given
        final CartItemEntity cartItem1 = CartItemEntity.of(1L, memberId, productId1, 1);
        final CartItemEntity cartItem2 = CartItemEntity.of(1L, memberId, productId2, 2);
        cartItemDao.create(cartItem1);
        cartItemDao.create(cartItem2);

        // when
        cartItemDao.deleteAllByMemberIdAndProductId(memberId, List.of(productId1, productId2));

        // then
        assertThat(cartItemDao.findByMemberIdAndProductId(memberId, productId1).isPresent()).isFalse();
        assertThat(cartItemDao.findByMemberIdAndProductId(memberId, productId2).isPresent()).isFalse();
    }
}
