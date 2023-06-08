package cart.persistence;

import cart.domain.cartitem.CartItemRepository;
import cart.persistence.cartitem.JdbcTemplateCartItemDao;
import cart.persistence.member.JdbcTemplateMemberDao;
import cart.domain.member.MemberRepository;
import cart.persistence.product.JdbcTemplateProductDao;
import cart.domain.product.ProductRepository;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class CartItemRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberRepository memberRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;

    private Long 하디_아이디;
    private Long 현구막_아이디;

    private Long 피자_아이디;
    private Long 샐러드_아이디;
    private Long 치킨_아이디;

    @BeforeEach
    void setUp() {
        memberRepository = new JdbcTemplateMemberDao(jdbcTemplate);
        productRepository = new JdbcTemplateProductDao(jdbcTemplate);
        cartItemRepository = new JdbcTemplateCartItemDao(jdbcTemplate);

        memberRepository.addMember(하디);
        memberRepository.addMember(현구막);
        하디_아이디 = memberRepository.findMemberByEmail(하디.getEmail()).get().getId();
        현구막_아이디 = memberRepository.findMemberByEmail(현구막.getEmail()).get().getId();

        피자_아이디 = productRepository.createProduct(피자);
        샐러드_아이디 = productRepository.createProduct(샐러드);
        치킨_아이디 = productRepository.createProduct(치킨);
    }

    @Test
    void 상품을_추가하고_조회한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(샐러드_아이디, 샐러드.getName(), 샐러드.getPrice(), 샐러드.getImageUrl(), 피자.getStock()));

        // when
        Long cartItemId = cartItemRepository.save(cartItem);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(cartItemRepository.findCartItemById(cartItemId))
                .isPresent();
        softAssertions.assertThat(cartItemRepository.findCartItemByMemberIdAndProductId(하디_아이디, 샐러드_아이디))
                .isPresent();
        softAssertions.assertAll();
    }

    @Test
    void 멤버의_전체상품을_조회한다() {
        // given
        CartItem firstCartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        CartItem secondCartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(치킨_아이디, 치킨.getName(), 치킨.getPrice(), 치킨.getImageUrl(), 치킨.getStock()));
        Long firstCartItemId = cartItemRepository.save(firstCartItem);
        Long secondCartItemId = cartItemRepository.save(secondCartItem);

        // when
        List<CartItem> cartItems = cartItemRepository.findAllCartItemsByMemberId(하디_아이디);
        List<String> productNames = cartItems.stream()
                .map(CartItem::getProduct)
                .map(Product::getName)
                .collect(Collectors.toList());
        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(cartItems.size()).isEqualTo(2);
        softAssertions.assertThat(productNames).containsAll(List.of(피자.getName(), 치킨.getName()));
        softAssertions.assertAll();
    }

    @Test
    void 멤버의_장바구니에_아무것도_없을_때_전체상품을_조회하면_빈_리스트를_반환한다() {
        // given, when
        List<CartItem> cartItems = cartItemRepository.findAllCartItemsByMemberId(현구막_아이디);

        // then
        assertThat(cartItems).isEmpty();
    }

    @Test
    void 상품의_수량을_업데이트한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemRepository.save(cartItem);

        Long updateQuantity = 30L;
        CartItem updateCartItem = new CartItem(
                cartItemId,
                updateQuantity,
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));

        // when
        cartItemRepository.updateQuantity(updateCartItem);

        // then
        assertThat(cartItemRepository.findCartItemById(cartItemId).get().getQuantity())
                .isEqualTo(updateQuantity);
    }

    @Test
    void 장바구니에서_상품을_장바구니_아이디로_삭제한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemRepository.save(cartItem);

        // when
        cartItemRepository.deleteById(cartItemId);

        // then
        assertThat(cartItemRepository.findCartItemById(cartItemId).isEmpty()).isTrue();
    }

    @Test
    void 장바구니에서_상품을_멤버아이디와_상품아이디로_삭제한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemRepository.save(cartItem);

        // when
        cartItemRepository.delete(하디_아이디, 피자_아이디);

        // then
        assertThat(cartItemRepository.findCartItemById(cartItemId).isEmpty()).isTrue();
    }
}
