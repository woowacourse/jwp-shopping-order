package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.CartItemException;
import cart.exception.CartItemNotFoundException;
import cart.exception.ProductNotFoundException;
import cart.fixture.CartItemFixture;
import cart.fixture.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class CartItemRepositoryTest {

    private CartItemDao cartItemDao;
    private CartItemRepository cartItemRepository;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        cartItemDao = new CartItemDao(jdbcTemplate);
        cartItemRepository = new CartItemRepository(new ProductDao(jdbcTemplate), cartItemDao);
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void addCartItem() {
        final CartItem result = cartItemRepository.addCartItem(new Member(1L, "odo1@woowa.com", "1234"), 1L);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.getMember().getEmail()).isEqualTo("odo1@woowa.com"),
                () -> assertThat(result.getMember().getPassword()).isEqualTo("1234"),
                () -> assertThat(result.getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getQuantity()).isEqualTo(1)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void addCartItemWithNoExistProductId() {
        assertThatThrownBy(() -> cartItemRepository.addCartItem(MemberFixture.MEMBER, 100L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage(new ProductNotFoundException(100L).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void findByMember() {
        final Long chickenId = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        final Long pizzaId = cartItemDao.insert(CartItemFixture.PIZZA).getId();
        final List<CartItem> result = cartItemRepository.findByMember(MemberFixture.MEMBER);
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(chickenId),
                () -> assertThat(result.get(0).getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getProduct().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(1),
                () -> assertThat(result.get(1).getId()).isEqualTo(pizzaId),
                () -> assertThat(result.get(1).getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getProduct().getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getQuantity()).isEqualTo(1)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void updateQuantity() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemRepository.updateQuantity(MemberFixture.MEMBER, id, 10);
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getQuantity()).isEqualTo(10)
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void updateQuantityWithNoExistId() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemDao.deleteById(id);
        assertThatThrownBy(() -> cartItemRepository.updateQuantity(MemberFixture.MEMBER, id, 10))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage(new CartItemNotFoundException(id).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void updateQuantityWithIllegalMember() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        final Member invalidMember = new Member(2L, "odo1@woowa.com", "1234");
        assertThatThrownBy(() -> cartItemRepository.updateQuantity(invalidMember, id, 10))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage(new CartItemException.IllegalMember(CartItem.of(id, null, null, 0), invalidMember).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void deleteCartItem() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemRepository.deleteCartItem(MemberFixture.MEMBER, id);
        final Optional<CartItem> result = cartItemDao.findById(id);
        assertThat(result).isEmpty();
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void deleteCartItemWithNoExistId() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        cartItemDao.deleteById(id);
        assertThatThrownBy(() -> cartItemRepository.deleteCartItem(MemberFixture.MEMBER, id))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage(new CartItemNotFoundException(id).getMessage());
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql"})
    @Test
    void deleteCartItemWithIllegalMember() {
        final Long id = cartItemDao.insert(CartItemFixture.CHICKEN).getId();
        final Member invalidMember = new Member(2L, "odo1@woowa.com", "1234");
        assertThatThrownBy(() -> cartItemRepository.deleteCartItem(invalidMember, id))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage(new CartItemException.IllegalMember(CartItem.of(id, null, null, 0), invalidMember).getMessage());
    }
}
