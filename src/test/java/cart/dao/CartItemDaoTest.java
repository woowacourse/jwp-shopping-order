package cart.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:data.sql", config = @SqlConfig(encoding = "utf-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    public void setup() {
        cartItemDao = new CartItemDao(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Test
    public void findByMemberId_ShouldReturnCartItems() {
        Long memberId = 1L;
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);

        assertThat(cartItems).isNotEmpty();
        assertThat(cartItems.size()).isEqualTo(2);
    }

    @Test
    public void save_ShouldAddCartItem() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        Product product = new Product(4L, "해왕성", 10000,
            "https://cdn.pixabay.com/photo/2020/09/06/22/11/neptune-5550216__480.jpg");
        CartItem cartItem = new CartItem(null, 3, product, member);

        Long savedItemId = cartItemDao.save(cartItem);
        assertThat(savedItemId).isNotNull();

        CartItem savedCartItem = cartItemDao.findById(savedItemId);
        assertThat(savedCartItem).isNotNull();
        assertThat(savedCartItem.getId()).isEqualTo(savedItemId);
        assertThat(savedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(savedCartItem.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedCartItem.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    void findByMemberId_ShouldReturnCartItemsForGivenMemberId() {
        Long memberId = 1L;
        List<Long> expectedCartItemIds = List.of(1L, 2L);

        List<Long> actualCartItemIds = cartItemDao.findByMemberId(memberId)
            .stream()
            .map(CartItem::getId)
            .collect(
                Collectors.toList());

        assertThat(actualCartItemIds).isEqualTo(expectedCartItemIds);
    }

    @Test
    void save_ShouldSaveCartItemAndReturnGeneratedId() {
        CartItem cartItem = new CartItem(1L, 2,
            new Product(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg"),
            new Member(1L, "a@a.com", null, "라잇"));

        Long generatedId = cartItemDao.save(cartItem);

        assertThat(generatedId).isNotNull();
    }

    @Test
    void findById_ShouldReturnCartItemForGivenId() {
        Long cartItemId = 1L;
        CartItem expectedCartItem = new CartItem(1L, 2,
            new Product(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg"),
            new Member(1L, "a@a.com", null, "라잇"));

        CartItem actualCartItem = cartItemDao.findById(cartItemId);

        assertThat(actualCartItem.getId()).isEqualTo(expectedCartItem.getId());
    }

    @Test
    void delete_ShouldDeleteCartItemForGivenMemberIdAndProductId() {
        Long memberId = 1L;
        Long productId = 1L;

        int size = cartItemDao.findByMemberId(1L).size();
        cartItemDao.delete(memberId, productId);

        assertThat(cartItemDao.findByMemberId(1L).size()).isEqualTo(size - 1);
    }
}

