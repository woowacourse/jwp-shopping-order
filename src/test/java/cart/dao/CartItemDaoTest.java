package cart.dao;

import cart.dao.cartitem.CartItemDao;
import cart.dao.cartitem.JdbcTemplateCartItemDao;
import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.dao.product.JdbcTemplateProductDao;
import cart.dao.product.ProductDao;
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
public class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private ProductDao productDao;
    private CartItemDao cartItemDao;

    private Long 하디_아이디;
    private Long 현구막_아이디;

    private Long 피자_아이디;
    private Long 샐러드_아이디;
    private Long 치킨_아이디;

    @BeforeEach
    void setUp() {
        memberDao = new JdbcTemplateMemberDao(jdbcTemplate);
        productDao = new JdbcTemplateProductDao(jdbcTemplate);
        cartItemDao = new JdbcTemplateCartItemDao(jdbcTemplate);

        memberDao.addMember(하디);
        memberDao.addMember(현구막);
        하디_아이디 = memberDao.findMemberByEmail(하디.getEmail()).get().getId();
        현구막_아이디 = memberDao.findMemberByEmail(현구막.getEmail()).get().getId();

        피자_아이디 = productDao.createProduct(피자);
        샐러드_아이디 = productDao.createProduct(샐러드);
        치킨_아이디 = productDao.createProduct(치킨);
    }

    @Test
    void 상품을_추가하고_조회한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(샐러드_아이디, 샐러드.getName(), 샐러드.getPrice(), 샐러드.getImageUrl(), 피자.getStock()));

        // when
        Long cartItemId = cartItemDao.save(cartItem);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(cartItemDao.findCartItemById(cartItemId))
                .isPresent();
        softAssertions.assertThat(cartItemDao.findCartItemByMemberIdAndProductId(하디_아이디, 샐러드_아이디))
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
        Long firstCartItemId = cartItemDao.save(firstCartItem);
        Long secondCartItemId = cartItemDao.save(secondCartItem);

        // when
        List<CartItem> cartItems = cartItemDao.findAllCartItemsByMemberId(하디_아이디);
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
        List<CartItem> cartItems = cartItemDao.findAllCartItemsByMemberId(현구막_아이디);

        // then
        assertThat(cartItems).isEmpty();
    }

    @Test
    void 상품의_수량을_업데이트한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemDao.save(cartItem);

        Long updateQuantity = 30L;
        CartItem updateCartItem = new CartItem(
                cartItemId,
                updateQuantity,
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));

        // when
        cartItemDao.updateQuantity(updateCartItem);

        // then
        assertThat(cartItemDao.findCartItemById(cartItemId).get().getQuantity())
                .isEqualTo(updateQuantity);
    }

    @Test
    void 장바구니에서_상품을_장바구니_아이디로_삭제한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemDao.save(cartItem);

        // when
        cartItemDao.deleteById(cartItemId);

        // then
        assertThat(cartItemDao.findCartItemById(cartItemId).isEmpty()).isTrue();
    }

    @Test
    void 장바구니에서_상품을_멤버아이디와_상품아이디로_삭제한다() {
        // given
        CartItem cartItem = new CartItem(
                new Member(하디_아이디, 하디.getEmail(), 하디.getPassword()),
                new Product(피자_아이디, 피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock()));
        Long cartItemId = cartItemDao.save(cartItem);

        // when
        cartItemDao.delete(하디_아이디, 피자_아이디);

        // then
        assertThat(cartItemDao.findCartItemById(cartItemId).isEmpty()).isTrue();
    }
}
