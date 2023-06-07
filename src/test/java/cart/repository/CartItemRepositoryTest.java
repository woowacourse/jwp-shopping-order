package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;
import cart.exception.ErrorMessage;
import java.util.List;
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
class CartItemRepositoryTest {
    private final ProductEntity 첫번째_상품_엔티티 = new ProductEntity(null, "치킨", 10_000, "http://example/chicken.png");
    private final ProductEntity 두번째_상품_엔티티 = new ProductEntity(null, "피자", 20_000, "http://example/chicken.png");

    private final MemberEntity 멤버_엔티티 = new MemberEntity(
            null, "vero@email", "password", 20000, null, null
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CartItemRepository cartItemRepository;
    private CartItemDao cartItemDao;
    private MemberDao memberDao;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);

        cartItemRepository = new CartItemRepository(cartItemDao);
    }

    @Test
    void 멤버_ID로_장바구니_상품들을_조회한다() {
        // given
        Product 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 첫번째_장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 첫번째_상품.getId(), 10));
        CartItem 두번째_장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 두번째_상품.getId(), 20));

        // when
        CartItems 상품들 = cartItemRepository.findByMemberId(멤버);

        // then
        assertAll(
                () -> assertThat(상품들.getCartItems()).hasSize(2),
                () -> assertThat(상품들.getCartItems()).usingRecursiveComparison()
                        .ignoringFields("member.password")
                        .isEqualTo(List.of(첫번째_장바구니_상품, 두번째_장바구니_상품))
        );
    }

    private Member 멤버를_저장하고_ID가_있는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 멤버_ID = memberDao.save(멤버_엔티티);
        return new Member(멤버_ID, 멤버_엔티티.getEmail(), 멤버_엔티티.getPassword(), 멤버_엔티티.getPoint());
    }

    private Product 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return new Product(저장된_상품_ID, 상품_엔티티.getName(), 상품_엔티티.getPrice(), 상품_엔티티.getImageUrl());
    }

    private CartItemEntity 장바구니_상품_엔티티를_생성한다(Long 멤버_ID, Long 상품_ID, int 수량) {
        return new CartItemEntity(null, 멤버_ID, 상품_ID, 수량, null, null);
    }

    private CartItem 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(Member 멤버, CartItemEntity 장바구니_상품_엔티티) {
        Long 저장된_장바구니_상품_ID = cartItemDao.save(장바구니_상품_엔티티);
        ProductEntity 상품_엔티티 = productDao.findById(장바구니_상품_엔티티.getProductId()).get();
        Product 상품 = new Product(장바구니_상품_엔티티.getProductId(), 상품_엔티티.getName(), 상품_엔티티.getPrice(),
                상품_엔티티.getImageUrl());

        return new CartItem(저장된_장바구니_상품_ID, 장바구니_상품_엔티티.getQuantity(), 상품, 멤버);
    }

    @Test
    void 멤버와_상품으로_장바구니_상품을_조회한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));

        // when
        CartItem 저장된_장바구니_상품 = cartItemRepository.findByMemberAndProduct(멤버, 상품);

        // then
        assertThat(저장된_장바구니_상품).usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(장바구니_상품);
    }

    @Test
    void 장바구니_상품_ID로_장바구니_상품을_조회한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));

        // when
        CartItem 저장된_장바구니_상품 = cartItemRepository.findById(장바구니_상품.getId());

        // then
        assertThat(저장된_장바구니_상품).usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(장바구니_상품);
    }

    @Test
    void 존재하지_않는_장바구니_상품_ID로_조회하면_예외를_반환한다() {
        // given
        long 존재하지_않는_상품_ID = Long.MAX_VALUE;
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버, 장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));

        // then
        assertThatThrownBy(() -> cartItemRepository.findById(존재하지_않는_상품_ID))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage());
    }

    @Test
    void 장바구니_상품_ID_여러개로_장바구니_상품들을_조회한다() {
        // given
        Product 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 첫번째_장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 첫번째_상품.getId(), 10));
        CartItem 두번째_장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 두번째_상품.getId(), 20));

        // when
        CartItems 상품들 = cartItemRepository.findByMemberAndCartItemIds(멤버,
                List.of(첫번째_장바구니_상품.getId(), 두번째_장바구니_상품.getId()));

        // then
        assertAll(
                () -> assertThat(상품들.getCartItems()).hasSize(2),
                () -> assertThat(상품들.getCartItems()).usingRecursiveComparison()
                        .ignoringFields("member.password")
                        .isEqualTo(List.of(첫번째_장바구니_상품, 두번째_장바구니_상품))
        );
    }

    @Test
    void 멤버와_상품으로_장바구니_상품이_존재하는지_확인한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);
        Product 존재하지_않는_상품 = new Product(Long.MAX_VALUE, "존재하지않는상품", 200000, "존재하지않는이미지");

        장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버, 장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));

        // then
        assertAll(
                () -> assertThat(cartItemRepository.existCartItem(멤버, 상품)).isTrue(),
                () -> assertThat(cartItemRepository.existCartItem(멤버, 존재하지_않는_상품)).isFalse()
        );
    }

    @Test
    void 장바구니_상품의_수량을_변경한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));
        CartItem 변경된_장바구니_상품 = new CartItem(장바구니_상품.getId(), 장바구니_상품.getQuantity() + 20, 장바구니_상품.getProduct(),
                장바구니_상품.getMember());

        // when
        cartItemRepository.updateQuantity(변경된_장바구니_상품);
        CartItem 저장된_장바구니_상품 = cartItemRepository.findById(장바구니_상품.getId());

        // then
        assertThat(저장된_장바구니_상품).usingRecursiveComparison()
                .ignoringFields("member.password")
                .isEqualTo(변경된_장바구니_상품);
    }

    @Test
    void 없는_ID로_장바구니_상품을_수정하려하면_예외를_반환한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);
        CartItem 존재하지_않는_장바구니_상품 = new CartItem(Long.MAX_VALUE, 1000, 상품, 멤버);

        // then
        assertThatThrownBy(() -> cartItemRepository.updateQuantity(존재하지_않는_장바구니_상품))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage());
    }

    @Test
    void 장바구니_상품을_삭제한다() {
        // given
        Product 상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID를_갖는_장바구니_상품을_리턴한다(멤버,
                장바구니_상품_엔티티를_생성한다(멤버.getId(), 상품.getId(), 10));

        // when
        cartItemRepository.deleteById(장바구니_상품.getId());

        // then
        assertThatThrownBy(() -> cartItemRepository.findById(장바구니_상품.getId()))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage());
    }

    @Test
    void 없는_ID로_장바구니_상품을_삭제하려하면_예외를_반환한다() {
        // given
        Long 존재하지_않는_장바구니_상품_ID = Long.MAX_VALUE;

        // then
        assertThatThrownBy(() -> cartItemRepository.deleteById(존재하지_않는_장바구니_상품_ID))
                .isInstanceOf(CartItemException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_ITEM.getMessage());
    }
}
