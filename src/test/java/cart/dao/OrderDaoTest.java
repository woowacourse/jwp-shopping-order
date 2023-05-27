package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
class OrderDaoTest {

    private OrderDao orderDao;
    private CartItemDao cartItemDao;
    private MemberDao memberDao;
    private ProductDao productDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @DisplayName("주문을 저장할 수 있다")
    @Test
    void save() {
        //given
        final Member member = createMember();
        final Product product = createProduct();

        final Long cartItemId1 = cartItemDao.save(new CartItem(member, product));
        final Long cartItemId2 = cartItemDao.save(new CartItem(member, product));
        final List<CartItem> cartItems = cartItemDao.findAllByIds(List.of(cartItemId1, cartItemId2));

        final Order order = new Order(member, 1000, cartItems);

        //when
        orderDao.save(order);

        //then
        assertSoftly(soft -> {
            assertThat(countRowsInTable(jdbcTemplate, "`order`")).isOne();
            assertThat(countRowsInTable(jdbcTemplate, "ordered_product")).isEqualTo(2);
        });
    }

    private Member createMember() {
        memberDao.addMember(new Member("abcd@naver.com", "1234"));
        return memberDao.getMemberByEmail("abcd@naver.com");
    }

    private Product createProduct() {
        final Long productId = productDao.createProduct(new Product("광어", 50000, "이미지url"));
        return new Product(productId, "광어", 50000, "이미지url");
    }
}
