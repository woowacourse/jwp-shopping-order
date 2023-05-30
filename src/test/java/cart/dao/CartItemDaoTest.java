package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ContextConfiguration(classes = CartItemDao.class)
public class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CartItemDao cartItemDao;

    private Long memberId;
    private final String MEMBER_EMAIL = "test@example.com";
    private Long productId1;
    private final String PRODUCT_NAME_1 = "test1";
    private Long productId2;
    private final String PRODUCT_NAME_2 = "test2";


    @DisplayName("cart item을 제외한 엔티티를 여기서 초기화 한다.")
    @BeforeEach
    void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, MEMBER_EMAIL);
            ps.setString(2, "test");

            return ps;
        }, keyHolder);
        memberId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, PRODUCT_NAME_1);
            ps.setInt(2, 1000);
            ps.setString(3, "test");
            ps.setInt(4, 10);

            return ps;
        }, keyHolder);
        productId1 = Objects.requireNonNull(keyHolder.getKey()).longValue();

        keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, PRODUCT_NAME_2);
            ps.setInt(2, 1000);
            ps.setString(3, "test");
            ps.setInt(4, 10);

            return ps;
        }, keyHolder);
        productId2 = Objects.requireNonNull(keyHolder.getKey()).longValue();

    }

    @DisplayName("memberId를 기준으로 장바구니를 조회한다.")
    @Test
    public void findByMemberId() {
        // given
        CartItem cartItem1 = new CartItem(new Member(memberId, MEMBER_EMAIL, null), new Product(productId1, PRODUCT_NAME_1, 0, null, 0));
        CartItem cartItem2 = new CartItem(new Member(memberId, MEMBER_EMAIL, null), new Product(productId2, PRODUCT_NAME_2, 0, null, 0));
        insertCartItem(cartItem1);
        insertCartItem(cartItem2);

        // when
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);

        // then
        assertEquals(cartItems.size(), 2);
    }

    @DisplayName("장바구니를 저장한다.")
    @Test
    public void save() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Product product = new Product(productId1, PRODUCT_NAME_1, 0, null, 0);
        CartItem cartItem = new CartItem(null, 2, product, member);

        // when
        Long savedItemId = cartItemDao.save(cartItem);

        // then
        CartItem item = cartItemDao.findById(savedItemId);

        assertEquals(item.getQuantity(), cartItem.getQuantity());
        assertEquals(item.getMember(), member);
        assertEquals(item.getProduct(), product);
    }

    @DisplayName("id를 기준으로 조회한다.")
    @Test
    public void findById() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Product product = new Product(productId2, PRODUCT_NAME_1, 0, null, 0);

        CartItem cartItem = new CartItem(null, 2, product, member);
        Long savedItemId = cartItemDao.save(cartItem);

        // when
        CartItem item = cartItemDao.findById(savedItemId);

        // then
        assertEquals(item.getQuantity(), cartItem.getQuantity());
        assertEquals(item.getMember(), member);
        assertEquals(item.getProduct(), product);
    }

    @DisplayName("memberId와 productId를 기준으로 조회한다.")
    @Test
    public void delete() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Product product = new Product(productId1, PRODUCT_NAME_1, 0, null, 0);
        CartItem cartItem = new CartItem(null, 2, product, member);
        Long savedItemId = cartItemDao.save(cartItem);

        // when
        cartItemDao.delete(memberId, productId1);

        // then
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cart_item", "id = " + savedItemId), 0);
    }

    @DisplayName("id를 기준으로 장바구니를 제거한다.")
    @Test
    public void deleteById() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Product product = new Product(productId1, PRODUCT_NAME_1, 0, null, 0);
        CartItem cartItem = new CartItem(null, 2, product, member);
        Long savedItemId = cartItemDao.save(cartItem);

        // when
        cartItemDao.deleteById(savedItemId);

        // then
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "cart_item", "id = " + savedItemId), 0);
    }

    @DisplayName("장바구니에 담은 수를 변경한다.")
    @Test
    public void updateQuantity() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Product product = new Product(productId1, PRODUCT_NAME_1, 0, null, 0);
        CartItem cartItem = new CartItem(null, 2, product, member);
        Long savedItemId = cartItemDao.save(cartItem);
        int newQuantity = 5;

        CartItem original = cartItemDao.findById(savedItemId);
        original.changeQuantity(newQuantity);

        // when
        cartItemDao.updateQuantity(original);
        CartItem updatedCartItem = cartItemDao.findById(savedItemId);

        // then
        assertEquals(updatedCartItem.getQuantity(), newQuantity);
    }

    @DisplayName("cartItem을 임의로 추가한다.")
    private Long insertCartItem(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
