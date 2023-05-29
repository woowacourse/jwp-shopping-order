package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    private MemberDao memberDao;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password1"));
        final ProductEntity product = productDao.insert(new ProductEntity("치즈피자1", "1.jpg", 8900L));
        final CartItemEntity cartItemEntity = new CartItemEntity(member.getId(), product.getId(), 1);

        // when
        cartItemDao.insert(cartItemEntity);

        // then
        assertThat(cartItemDao.findAllByMemberId(member.getId())).hasSize(1);
    }

    @Test
    void 사용자_아이디를_받아_해당_사용자의_장바구니에_있는_모든_품목을_반환한다() {
        // given
        final MemberEntity member1 = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password"));
        final MemberEntity member2 = memberDao.insert(new MemberEntity("pizza2@pizza.com", "password"));
        final ProductEntity product1 = productDao.insert(new ProductEntity("치즈피자1", "1.jpg", 8900L));
        final ProductEntity product2 = productDao.insert(new ProductEntity("치즈피자2", "2.jpg", 8900L));
        final ProductEntity product3 = productDao.insert(new ProductEntity("치즈피자3", "3.jpg", 8900L));

        cartItemDao.insert(new CartItemEntity(member1.getId(), product1.getId(), 1));
        cartItemDao.insert(new CartItemEntity(member1.getId(), product2.getId(), 1));
        cartItemDao.insert(new CartItemEntity(member2.getId(), product3.getId(), 1));

        // when
        final List<CartItemEntity> result = cartItemDao.findAllByMemberId(member1.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 사용자_아이디와_삭제할_장바구니의_상품_아이디를_받아_장바구니_항목을_제거한다() {
        // given
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password1"));
        final ProductEntity product = productDao.insert(new ProductEntity("치즈피자1", "1.jpg", 8900L));
        final CartItemEntity cartItemEntity = new CartItemEntity(member.getId(), product.getId(), 1);

        // when
        cartItemDao.deleteById(cartItemEntity.getId(), member.getId());

        // then
        assertThat(cartItemDao.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 장바구니의_상품의_수량을_변경한다() {
        // given
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password1"));
        final ProductEntity product = productDao.insert(new ProductEntity("치즈피자1", "1.jpg", 8900L));
        final CartItemEntity cartItemEntity = new CartItemEntity(member.getId(), product.getId(), 1);
        final CartItemEntity savedCartItemEntity = cartItemDao.insert(cartItemEntity);

        final CartItemEntity updatedCartItemEntity = new CartItemEntity(
                savedCartItemEntity.getId(),
                savedCartItemEntity.getMemberId(),
                savedCartItemEntity.getProductId(),
                2
        );

        // when
        cartItemDao.updateQuantity(updatedCartItemEntity);

        // then
        final CartItemEntity result = cartItemDao.findById(updatedCartItemEntity.getId()).get();
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    void 해당멤버_아이디와_장바구니_상품_아이디로_장바구니_상품들을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password"));
        final ProductEntity product1 = productDao.insert(new ProductEntity("치즈피자1", "1.jpg", 8900L));
        final ProductEntity product2 = productDao.insert(new ProductEntity("치즈피자2", "2.jpg", 8900L));
        final ProductEntity product3 = productDao.insert(new ProductEntity("치즈피자3", "3.jpg", 8900L));

        CartItemEntity savedCartItemEntity1 = cartItemDao.insert(new CartItemEntity(member.getId(), product1.getId(), 1));
        CartItemEntity savedCartItemEntity2 = cartItemDao.insert(new CartItemEntity(member.getId(), product2.getId(), 1));
        CartItemEntity savedCartItemEntity3 = cartItemDao.insert(new CartItemEntity(member.getId(), product3.getId(), 1));

        // when
        List<CartItemEntity> result = cartItemDao.findAllByCartItemIds(
                List.of(savedCartItemEntity1.getId(), savedCartItemEntity2.getId()));

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedCartItemEntity1, savedCartItemEntity2));
    }
}
