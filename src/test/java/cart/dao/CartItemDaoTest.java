package cart.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@JdbcTest
class CartItemDaoTest {

    private CartItemDao cartItemDao;

    private CartItem cartItem1;
    private CartItem cartItem2;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void init() {
        cartItemDao = new CartItemDao(jdbcTemplate, namedParameterJdbcTemplate);
        ProductDao productDao = new ProductDao(jdbcTemplate);
        MemberDao memberDao = new MemberDao(jdbcTemplate);

        Product product1 = new Product("product1", 100, "image1");
        Product product2 = new Product(2L, "product2", 100, "image2");

        Member member1 = new Member("email1", "123");
        Member member2 = new Member("email2", "123");

        Long product1Id = productDao.createProduct(product1);
        Long product2Id = productDao.createProduct(product2);

        memberDao.addMember(member1);
        memberDao.addMember(member2);

        cartItem1 = new CartItem(getProductWithId(product1, product1Id),
                memberDao.getMemberByEmail(member1.getEmail()));
        cartItem2 = new CartItem(getProductWithId(product2, product2Id),
                memberDao.getMemberByEmail(member2.getEmail()));
    }

    private Product getProductWithId(final Product product1, final Long product1Id) {
        return new Product(product1Id, product1.getName(), product1.getPrice(), product1.getImageUrl());
    }


    @DisplayName("findByIds에 장바구니 상품 ID들을 넣으면 해당 장바구니 상품 List를 반환한다.")
    @Test
    void findByIds() {
        Long cartItem1id = cartItemDao.save(cartItem1);
        Long cartItem2id = cartItemDao.save(cartItem2);
        List<Long> ids = List.of(cartItem1id, cartItem2id);

        List<CartItem> cartItems = cartItemDao.findByIds(ids);

        CartItem resultCartItem1 = cartItems.get(0);
        CartItem resultCartItem2 = cartItems.get(1);

        assertAll(
                () -> assertThat(resultCartItem1.getQuantity()).isEqualTo(cartItem1.getQuantity()),
                () -> assertThat(resultCartItem1.getMember()).isEqualTo(cartItem1.getMember()),
                () -> assertThat(resultCartItem1.getProduct()).isEqualTo(cartItem1.getProduct()),
                () -> assertThat(resultCartItem2.getQuantity()).isEqualTo(cartItem2.getQuantity()),
                () -> assertThat(resultCartItem2.getMember()).isEqualTo(cartItem2.getMember()),
                () -> assertThat(resultCartItem2.getProduct()).isEqualTo(cartItem2.getProduct())
        );
    }

}