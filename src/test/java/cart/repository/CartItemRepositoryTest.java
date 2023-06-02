package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.dao.CartItemDao;
import cart.dao.dto.CartItemProductDto;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.exception.CartItemNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    @Mock
    private CartItemDao cartItemDao;

    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        this.cartItemRepository = new CartItemRepository(cartItemDao);
    }

    @Test
    @DisplayName("장바구니 id 가 존재하지 않는다면 예외가 발생한다.")
    void findById() {
        // given
        given(cartItemDao.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> cartItemRepository.findById(1L))
            .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("사용자의 전체 장바구니를 조회할 수 있다.")
    void findByMember() {
        // given
        Member member = new Member(1L, "email", null);
        given(cartItemDao.findByMemberId(anyLong())).willReturn(
            List.of(
                new CartItemProductDto(1L, 1L, member.getId(),
                    member.getEmail(), 3, "productName",
                    1000, "imgUrl")
            ));

        // when
        Cart cart = cartItemRepository.findByMember(member);

        // then
        assertThat(cart).usingRecursiveComparison()
            .isEqualTo(new Cart(
                List.of(
                    new CartItem(1L, Quantity.from(3),
                        new Product(1L, "productName", Money.from(1000), "imgUrl"),
                        member)
                )
            ));
    }

    @Test
    @DisplayName("장바구니 상품을 삭제할 때, 존재하지 않는 id 라면 예외가 발생한다.")
    void delete_fail() {
        // given
        CartItem cartItem = new CartItem(1L, Quantity.DEFAULT,
            new Product("productName", Money.from(1000), "imgUrl"),
            new Member(1L, "email", "password"));
        given(cartItemDao.isNonExistingId(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> cartItemRepository.delete(cartItem))
            .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("장바구니 수량을 수정할 때, 존재하지 않는 id 라면 예외가 발생한다.")
    void updateQuantity_fail() {
        // given
        CartItem cartItem = new CartItem(1L, Quantity.DEFAULT,
            new Product("productName", Money.from(1000), "imgUrl"),
            new Member(1L, "email", "password"));
        given(cartItemDao.isNonExistingId(anyLong())).willReturn(true);

        // when
        assertThatThrownBy(() -> cartItemRepository.updateQuantity(cartItem))
            .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("id 리스트로 여러 장바구니 상품을 조회할 때, 존재하지 않는 id 가 포함되어 있다면 예외가 발생한다.")
    void findAllByIds_fail() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);
        given(cartItemDao.findAllByIds(anyList())).willReturn(
            List.of(
                new CartItemProductDto(1L, 1L, 1L,
                    "email", 3, "productName",
                    1000, "imgUrl"),
                new CartItemProductDto(2L, 1L, 1L,
                    "email", 3, "productName",
                    1000, "imgUrl")
            ));

        // when
        assertThatThrownBy(() -> cartItemRepository.findAllByIds(ids))
            .isInstanceOf(CartItemNotFoundException.class);
    }

}
