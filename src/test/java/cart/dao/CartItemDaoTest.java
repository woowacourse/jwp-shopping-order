package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;
    private MemberDao memberDao;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);

        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void 장바구니_추가_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixture = getSavedProductFixture("testProduct");

        //when
        cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixture));

        //then
        final List<CartItem> allItems = cartItemDao.findAll();
        assertThat(allItems).hasSize(1);
        assertThat(allItems.get(0).getQuantity()).isEqualTo(3);
        assertThat(allItems.get(0).getProduct().getName()).isEqualTo("testProduct");
    }

    @Test
    void 사용자_장바구니_조회_테스트() {
        //given
        final Member memberFixtureA = getSavedMemberFixture("testEmailA");
        final Product productFixtureA = getSavedProductFixture("testProductA");
        final Product productFixtureB = getSavedProductFixture("testProductB");
        cartItemDao.saveCartItem(new CartItem(1, memberFixtureA, productFixtureA));
        cartItemDao.saveCartItem(new CartItem(3, memberFixtureA, productFixtureB));

        final Member memberFixtureB = getSavedMemberFixture("testEmailB");
        final Product productFixtureC = getSavedProductFixture("testProductC");
        cartItemDao.saveCartItem(new CartItem(2, memberFixtureB, productFixtureC));

        //when
        final List<CartItem> byMemberCartItems = cartItemDao.findByMemberId(memberFixtureA.getId());

        //then
        assertThat(byMemberCartItems).hasSize(2);
    }

    @Test
    void 장바구니_상품_식별자로_조회_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixture = getSavedProductFixture("testProduct");
        final Long savedCartItemId = cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixture));

        //when
        final Optional<CartItem> byId = cartItemDao.findById(savedCartItemId);

        //then
        assertThat(byId).isNotEmpty();
        assertThat(byId.get().getMember().getEmail()).isEqualTo("testEmail");
    }


    @Test
    void 장바구니_상품_식별자로_삭제_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixture = getSavedProductFixture("testProduct");
        final Long savedCartItemId = cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixture));

        //when
        cartItemDao.deleteById(savedCartItemId);

        //then
        final List<CartItem> all = cartItemDao.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void 장바구니_상품_개수_변경_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixture = getSavedProductFixture("testProduct");
        final Long savedCartItemId = cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixture));
        final int expectedQuantity = 15;

        //when
        cartItemDao.updateQuantity(savedCartItemId, expectedQuantity);

        //then
        final Optional<CartItem> byId = cartItemDao.findById(savedCartItemId);
        assertThat(byId).isNotEmpty();
        assertThat(byId.get().getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void 장바구니_상품_선택_조회_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixtureA = getSavedProductFixture("testProductA");
        final Product productFixtureB = getSavedProductFixture("testProductB");
        final Product productFixtureC = getSavedProductFixture("testProductC");
        final Long savedCartItemAId = cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixtureA));
        final Long savedCartItemBId = cartItemDao.saveCartItem(new CartItem(1, memberFixture, productFixtureB));
        cartItemDao.saveCartItem(new CartItem(5, memberFixture, productFixtureC));

        //when
        final List<CartItem> cartItemsByIds = cartItemDao.findByIds(List.of(savedCartItemAId, savedCartItemBId));

        //then
        assertThat(cartItemsByIds).hasSize(2);
        assertThat(cartItemsByIds.get(0).getProduct().getName()).isNotEqualTo("testProductC");
        assertThat(cartItemsByIds.get(1).getProduct().getName()).isNotEqualTo("testProductC");
    }

    @Test
    void 장바구니_상품_선택_삭제_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Product productFixtureA = getSavedProductFixture("testProductA");
        final Product productFixtureB = getSavedProductFixture("testProductB");
        final Product productFixtureC = getSavedProductFixture("testProductC");
        final Long savedCartItemAId = cartItemDao.saveCartItem(new CartItem(3, memberFixture, productFixtureA));
        final Long savedCartItemBId = cartItemDao.saveCartItem(new CartItem(1, memberFixture, productFixtureB));
        cartItemDao.saveCartItem(new CartItem(5, memberFixture, productFixtureC));

        //when
        cartItemDao.deleteById(List.of(savedCartItemAId, savedCartItemBId));

        //then
        final List<CartItem> all = cartItemDao.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getProduct().getName()).isEqualTo("testProductC");
    }

    private Member getSavedMemberFixture(final String email) {
        final Member member = new Member(email, "password");
        final Long saveMemberId = memberDao.saveMember(member);

        return new Member(saveMemberId, member.getEmail(), member.getPassword());
    }

    private Product getSavedProductFixture(final String productName) {
        final Product product = new Product(productName, 3_000, "testImage");
        final Long savedProductId = productDao.saveProduct(product);
        return new Product(savedProductId, product.getName(), product.getPrice(), product.getImageUrl());
    }
}
