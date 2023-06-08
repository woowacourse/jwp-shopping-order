package cart.persistence.repository;

import cart.entity.CartItem;
import cart.entity.Member;
import cart.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:truncate.sql", "classpath:data.sql"})
class CartItemPersistenceAdapterTest {

    private CartItemPersistenceAdapter cartItemPersistenceAdapter;

    private Product product;
    private Member member;

    @Autowired
    public CartItemPersistenceAdapterTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.cartItemPersistenceAdapter = new CartItemPersistenceAdapter(namedParameterJdbcTemplate);

        this.member = new Member(1L, "a@a.com", "1234", 20000);
        this.product = new Product(1L, "치킨", 10000, "https://", 10.0, true);
    }

    @Test
    @DisplayName("장바구니 품목을 삽입할 수 있다")
    void insert() {
        // given, when
        CartItem inserted = cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // then
        assertThat(inserted).isNotNull();
    }

    @Test
    @DisplayName("장바구니 품목을 찾을 수 있다")
    void findById() {
        // given
        CartItem inserted = cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // when
        Optional<CartItem> found = cartItemPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("장바구니 품목을 멤버 ID를 통해 찾을 수 있다")
    void findByMemberId() {
        // given
        cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // when
        List<CartItem> found = cartItemPersistenceAdapter.findByMemberId(member.getId());
        // then
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("장바구니 품목을 업데이트 할 수 있다")
    void update() {
        // given
        CartItem inserted = cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // when
        cartItemPersistenceAdapter.update(new CartItem(inserted.getId(), 2L, product, member));
        Optional<CartItem> found = cartItemPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found.get().getQuantity()).isEqualTo(2L);
    }

    @Test
    @DisplayName("ID를 통해 삭제할 수 있다")
    void deleteById() {
        // given
        CartItem inserted = cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // when
        cartItemPersistenceAdapter.deleteById(inserted.getId());
        Optional<CartItem> found = cartItemPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("멤버 ID를 통해 삭제할 수 있다")
    void deleteByMemberId() {
        // given
        CartItem inserted = cartItemPersistenceAdapter.insert(new CartItem(null, 1L, product, member));
        // when
        cartItemPersistenceAdapter.deleteByMemberId(member.getId());
        List<CartItem> found = cartItemPersistenceAdapter.findByMemberId(member.getId());
        // then
        assertThat(found).isEmpty();
    }
}
