package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.MemberNotExistException;
import java.util.List;
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
    private MemberDao memberDao;
    private CartItemDao cartItemDao;
    private ProductDao productDao;
    private Product product1;
    private Product product2;
    private Member member;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);

        this.member = findMemberById(1L);
        this.product1 = createProduct("치킨", 10_000, "http://example.com/chicken.jpg");
        this.product2 = createProduct("피자", 20_000, "http://example.com/pizza.jpg");

    }

    @DisplayName("장바구니 상품을 DB에 저장한다.")
    @Test
    void insert() {
        //given
        final CartItem cartItem = new CartItem(member, product1);

        //when
        final CartItem persistedCartItem = cartItemDao.insert(cartItem);

        //then
        final CartItem findCartItem = cartItemDao.findById(persistedCartItem.getId()).get();
        assertAll(
            () -> assertThat(findCartItem.getId()).isEqualTo(persistedCartItem.getId()),
            () -> assertThat(findCartItem.getMember().getId()).isEqualTo(persistedCartItem.getMember().getId()),
            () -> assertThat(findCartItem.getProduct().getId()).isEqualTo(persistedCartItem.getProduct().getId()),
            () -> assertThat(findCartItem.getQuantity()).isEqualTo(persistedCartItem.getQuantity())
        );
    }

    @DisplayName("해당 멤버의 장바구니 상품을 모두 조회한다.")
    @Test
    void findByMemberId() {
        //given
        final CartItem cartItem = cartItemDao.insert(new CartItem(member, product1));

        //when
        final List<CartItem> memberCartItems = cartItemDao.findByMemberId(member.getId());

        //then
        assertThat(memberCartItems).contains(cartItem);
    }

    @DisplayName("해당 ID의 장바구니 상품을 모두 조회한다.")
    @Test
    void findById() {
        //given
        final CartItem cartItem = cartItemDao.insert(new CartItem(member, product1));

        //when
        final CartItem findCartItem = cartItemDao.findById(cartItem.getId()).get();

        //then
        assertThat(findCartItem).isEqualTo(cartItem);
    }

    @DisplayName("장바구니 상품의 수량을 업데이트한다.")
    @Test
    void updateQuantity() {
        //given
        final CartItem cartItem = cartItemDao.insert(new CartItem(null, 1, product1, member));
        cartItem.changeQuantity(10);

        //when
        cartItemDao.updateQuantity(cartItem);

        //then
        assertThat(cartItemDao.findById(cartItem.getId()).get().getQuantity()).isEqualTo(10);
    }

    @DisplayName("멤버와 상품의 ID에 포함되는 장바구니 상품을 삭제한다.")
    @Test
    void delete() {
        //given
        final List<CartItem> beforeSave = cartItemDao.findByMemberId(member.getId());
        cartItemDao.insert(new CartItem(member, product1));
        cartItemDao.insert(new CartItem(member, product2));

        //when
        cartItemDao.delete(member.getId(), product1.getId());
        cartItemDao.delete(member.getId(), product2.getId());

        //then
        final List<CartItem> byMemberId = cartItemDao.findByMemberId(member.getId());
        assertThat(byMemberId).usingRecursiveComparison().isEqualTo(beforeSave);
    }

    @DisplayName("멤버와 상품의 ID들에 포함되는 모든 장바구니 상품을 삭제한다.")
    @Test
    void deleteByMemberIdAndProductIds() {
        //given
        final List<CartItem> beforeSave = cartItemDao.findByMemberId(member.getId());
        cartItemDao.insert(new CartItem(member, product1));
        cartItemDao.insert(new CartItem(member, product2));

        //when
        cartItemDao.deleteByMemberIdAndProductIds(member.getId(), List.of(product1.getId(), product2.getId()));

        //then
        final List<CartItem> byMemberId = cartItemDao.findByMemberId(member.getId());
        assertThat(byMemberId).usingRecursiveComparison().isEqualTo(beforeSave);
    }

    @DisplayName("해당 ID에 포함되는 장바구니 상품을 삭제한다.")
    @Test
    void deleteById() {
        //given
        final CartItem cartItem = cartItemDao.insert(new CartItem(member, product1));

        //when
        cartItemDao.deleteById(cartItem.getId());

        //then
        assertThat(cartItemDao.findById(cartItem.getId())).isEmpty();
    }

    private Member findMemberById(final Long memberId) {
        return memberDao.getMemberById(1L).orElseThrow(() -> new MemberNotExistException("멤버가 존재하지 않습니다."));
    }

    private Product createProduct(final String name, final int price, final String imageUrl) {
        final Product product = new Product(name, price, imageUrl);
        return productDao.createProduct(product);
    }

}
