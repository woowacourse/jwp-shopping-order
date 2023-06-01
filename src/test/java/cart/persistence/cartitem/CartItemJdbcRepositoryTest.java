package cart.persistence.cartitem;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.persistence.member.MemberJdbcRepository;
import cart.persistence.product.ProductJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.CartItemFixture.레오_레오배변패드;
import static cart.fixture.MemberFixture.레오;
import static cart.fixture.ProductFixture.배변패드;
import static cart.fixture.ProductFixture.통구이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
class CartItemJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private MemberRepository memberRepository;

    private Long memberId;
    private Member member;

    private Long padId;
    private Long bbqId;
    private Product pad;
    private Product bbq;
    private CartItem padCartItem;
    private CartItem bbqCartItem;

    private Long padCartItemId;
    private Long bbqCartItemId;

    @BeforeEach
    void setUp() {
        cartItemRepository = new CartItemJdbcRepository(jdbcTemplate);
        productRepository = new ProductJdbcRepository(jdbcTemplate);
        memberRepository = new MemberJdbcRepository(jdbcTemplate);

        memberId = memberRepository.createMember(레오);
        member = new Member(memberId, 레오.getName(), 레오.getEmail(), 레오.getPassword());

        padId = productRepository.createProduct(배변패드);
        bbqId = productRepository.createProduct(통구이);
        pad = new Product(padId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());
        bbq = new Product(bbqId, 통구이.getName(), 통구이.getPrice(), 통구이.getImageUrl());

        padCartItem = new CartItem(2, pad, member);
        bbqCartItem = new CartItem(1, bbq, member);
        padCartItemId = cartItemRepository.createCartItem(padCartItem);
        bbqCartItemId = cartItemRepository.createCartItem(bbqCartItem);
    }

    @Test
    @DisplayName("상품을 장바구니에 올바르게 담는다.")
    void createCartItemTest() {
        // given
        final CartItem cartItem = 레오_레오배변패드;
        // when, then
        assertDoesNotThrow(() -> cartItemRepository.createCartItem(cartItem));
    }

    @Test
    @DisplayName("사용자의 장바구니 정보를 전체 조회한다.")
    void findAllCartItemsByMemberIdTest() {
        // when
        CartItems cartItems = cartItemRepository.findAllCartItemsByMemberId(memberId);

        assertThat(cartItems.getCartItems()).usingRecursiveComparison().ignoringFields("id").isEqualTo(List.of(padCartItem, bbqCartItem));
    }

    @Test
    @DisplayName("장바구니 id로 정보를 조회한다.")
    void findByIdTest() {
        // when
        final CartItem result = cartItemRepository.findById(padCartItemId).get();

        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(padCartItem);
    }

    @Test
    @DisplayName("장바구니 수량을 변경한다.")
    void updateQuantityTest() {
        // when
        int originalQuantity = bbqCartItem.getQuantity();
        CartItem newCartItem = new CartItem(bbqCartItemId, originalQuantity + 1, bbqCartItem.getProduct(), bbqCartItem.getMember());
        cartItemRepository.updateQuantity(newCartItem);
        final CartItem result = cartItemRepository.findById(bbqCartItemId).get();

        assertThat(result.getQuantity()).isEqualTo(originalQuantity + 1);
    }

    @Test
    @DisplayName("장바구니 아이템을 삭제한다.")
    void deleteByIdTest() {
        // when
        cartItemRepository.deleteById(padCartItemId);

        Optional<CartItem> cartItem = cartItemRepository.findById(padCartItemId);

        // then
        assertThat(cartItem).isEmpty();
    }

}
