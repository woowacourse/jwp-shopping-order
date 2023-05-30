package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;
    private ProductDao productDao;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 장바구니에_상품을_저장한다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();

        // when
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // then
        assertThat(cartItemId).isPositive();
    }

    @Test
    void 사용자_id로_장바구니_상품을_조회한다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        List<CartItemEntity> cartItems = cartItemDao.findByMemberId(member.getId());

        // then
        assertThat(cartItems).hasSize(1);
    }

    @Test
    void 장바구니_상품을_조회할_때_삭제된_상품이_안나온다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);
        productDao.deleteProduct(product.getId());

        // when
        List<CartItemEntity> cartItems = cartItemDao.findByMemberId(member.getId());

        // then
        assertThat(cartItems).hasSize(0);
    }

    @Test
    void 장바구니_상품을_id로_조회한다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        Optional<CartItemEntity> savedCartItem = cartItemDao.findById(cartItemId);

        // then
        assertThat(savedCartItem).isPresent();
    }

    @Test
    void 장바구니_상품을_삭제한다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        cartItemDao.deleteById(cartItemId);

        // then
        assertThat(cartItemDao.findById(cartItemId)).isEmpty();
    }

    @Test
    void 장바구니_상품_수량을_변경한다() {
        // given
        ProductEntity product = getProductEntity();
        MemberEntity member = getMemberEntity();
        CartItemEntity cartItemEntity = new CartItemEntity(product, member);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        cartItemDao.updateQuantity(new CartItemEntity(cartItemId, product, member, 100));

        // then
        CartItemEntity savedCartItem = cartItemDao.findById(cartItemId).get();
        assertThat(savedCartItem.getQuantity()).isEqualTo(100);
    }

    private MemberEntity getMemberEntity() {
        Long memberId = memberDao.save(new MemberEntity("email1@email.com", "password"));
        return memberDao.findById(memberId).get();
    }

    private ProductEntity getProductEntity() {
        Long productId = productDao.save(
                new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com"));
        return productDao.findById(productId).get();
    }
}
