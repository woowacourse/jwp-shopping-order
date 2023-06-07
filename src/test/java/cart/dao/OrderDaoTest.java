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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("주문 Id로 주문을 조회할 수 있다")
    @Test
    void findById() {
        //given
        final Member member = createMember();
        final Product product = createProduct();

        final Long cartItemId1 = cartItemDao.save(new CartItem(member, product));
        final Long cartItemId2 = cartItemDao.save(new CartItem(member, product));
        final List<CartItem> cartItems = cartItemDao.findAllByIds(List.of(cartItemId1, cartItemId2));

        final Order order = new Order(member, 1000, cartItems);
        final Order persisted = orderDao.save(order);

        //when
        final Order result = orderDao.findById(persisted.getId());

        //then
        assertSoftly(soft -> {
            assertThat(result.getMember()).isEqualTo(member);
            assertThat(result.getTotalPrice()).isEqualTo(1000);
            assertThat(result.getCartItems()).hasSize(2);
        });
    }

    @DisplayName("존재하지 않는 주문 Id로 주문을 조회하면 예외가 발생한다")
    @Test
    void findById_notExist() {
        assertThatThrownBy(() -> orderDao.findById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 정보입니다.");
    }

    @DisplayName("유저의 모든 주문 정보들을 조회할 수 있다")
    @Test
    void findAllByMemberId() {
        //given
        final Member member = createMember();
        final Product product = createProduct();

        final Long cartItemId1 = cartItemDao.save(new CartItem(member, product));
        final Long cartItemId2 = cartItemDao.save(new CartItem(member, product));
        final List<CartItem> cartItems = cartItemDao.findAllByIds(List.of(cartItemId1, cartItemId2));

        final Order order = new Order(member, 1000, cartItems);

        orderDao.save(order);
        orderDao.save(order);

        //when
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());

        //then
        assertSoftly(soft -> {
            assertThat(orders).hasSize(2);
            assertThat(orders).allMatch(o -> o.getMember().equals(member));
            assertThat(orders).allMatch(o -> o.getTotalPrice() == 1000);
            assertThat(orders).allMatch(o -> o.getCartItems().size() == 2);
        });
    }

    @DisplayName("유저의 주문 정보가 없을 때 주문 목록을 조회하면 빈 목록이 조회된다")
    @Test
    void findAllByMemberId_notExist() {
        //given
        final Member member = createMember();

        //when
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());

        //then
        assertThat(orders).isEmpty();
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
