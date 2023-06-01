package cart.dao;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.PIZZA;
import static cart.fixture.DomainFixture.TWO_CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    CartItemDao cartItemDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("save는 저장할 장바구니 상품을 전달하면 해당 장바구니 상품을 저장하고 ID를 반환한다.")
    void saveSuccessTest() {
        Long actual = cartItemDao.save(TWO_CHICKEN);

        assertThat(actual).isPositive();
    }

    @Test
    @DisplayName("findByMemberId는 회원 ID를 전달하면 해당 회원의 모든 장바구니 상품을 반환한다.")
    void findByMemberIdSuccessTest() {
        List<CartItem> actual = cartItemDao.findByMemberId(MEMBER_A.getId());

        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("findById는 장바구니 상품 ID를 전달하면 해당 장바구니 상품을 반환한다.")
    void findByIdSuccessTest() {
        Optional<CartItem> actual = cartItemDao.findById(TWO_CHICKEN.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(TWO_CHICKEN.getId())
        );
    }

    @Test
    @DisplayName("isExistBy는 회원 ID와 장바구니에 저장한 상품 ID를 전달하면 true를 반환한다.")
    void isExistByTestByTrue() {
        boolean actual = cartItemDao.isExistBy(MEMBER_A.getId(), CHICKEN.getId());

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("isExistBy는 회원 ID와 장바구니에 저장한 상품 ID를 전달하면 false를 반환한다.")
    void isExistByTestByFalse() {
        boolean actual = cartItemDao.isExistBy(MEMBER_A.getId(), PIZZA.getId());

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("updateQuantity는 수량을 변경할 장바구니 상품을 전달하면 해당 수량으로 변경한다.")
    void updateQuantitySuccessTest() {
        CartItem updateQuantityCartItem = TWO_CHICKEN;
        updateQuantityCartItem.changeQuantity(3);

        cartItemDao.updateQuantity(updateQuantityCartItem);

        CartItem actual = cartItemDao.findById(updateQuantityCartItem.getId()).get();

        assertThat(actual.getQuantity()).isEqualTo(updateQuantityCartItem.getQuantity());
    }

    @Test
    @DisplayName("deleteById는 존재하는 상품 ID를 전달하면 해당 ID를 삭제한다.")
    void deleteByIdSuccessTest() {
        cartItemDao.deleteById(TWO_CHICKEN.getId());

        Optional<CartItem> actual = cartItemDao.findById(TWO_CHICKEN.getId());

        assertThat(actual).isEmpty();
    }
}
