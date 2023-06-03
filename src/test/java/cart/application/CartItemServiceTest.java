package cart.application;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.FOUR_SALAD;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static cart.fixture.DomainFixture.PIZZA;
import static cart.fixture.DomainFixture.TWO_CHICKEN;
import static cart.fixture.DtoFixture.PIZZA_CART_ITEM_REQUEST;
import static cart.fixture.DtoFixture.THREE_QUANTITY_REQUEST;
import static cart.fixture.RepositoryFixture.cartItemRepository;
import static cart.fixture.RepositoryFixture.productRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dto.response.CartItemResponse;
import cart.dto.response.CheckoutResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemServiceTest {

    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    CartItemService cartItemService;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        productRepository = productRepository(jdbcTemplate);
        cartItemRepository = cartItemRepository(jdbcTemplate);
        cartItemService = new CartItemService(productRepository, cartItemRepository);
    }

    @Test
    @DisplayName("findByMember는 회원 전달하면 해당 회원의 모든 주문을 반환한다.")
    void findByMemberTest() {
        List<CartItemResponse> actual = cartItemService.findByMember(MEMBER_A);

        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("add는 회원과 장바구니에 존재하지 않는 상품을 전달하면 해당 회원의 장바구니에 상품을 추가한다.")
    void addSuccessTest() {
        Long actual = cartItemService.add(MEMBER_A, PIZZA_CART_ITEM_REQUEST);

        assertThat(actual).isPositive();
    }

    @Test
    @DisplayName("add는 해당 회원의 장바구니에 이미 존재하는 상품을 전달하면 예외가 발생한다.")
    void addFailTest() {
        cartItemService.add(MEMBER_A, PIZZA_CART_ITEM_REQUEST);

        assertThatThrownBy(() -> cartItemService.add(MEMBER_A, PIZZA_CART_ITEM_REQUEST))
                .isInstanceOf(CartItemException.AlreadyExist.class)
                .hasMessageContaining("의 장바구니에 이미 ");
    }

    @Test
    @DisplayName("updateQuantity는 회원과 장바구니 상품 ID와 변경하고자 하는 상품 수량을 전달하면 해당 상품의 수량을 변경한다.")
    void updateQuantitySuccessTest() {
        cartItemService.updateQuantity(MEMBER_A, CHICKEN.getId(), THREE_QUANTITY_REQUEST);

        CartItemResponse actual = cartItemService.findByMember(MEMBER_A)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(CHICKEN.getId()))
                .findFirst().get();
        assertThat(actual.getQuantity()).isEqualTo(THREE_QUANTITY_REQUEST.getQuantity());
    }

    @Test
    @DisplayName("updateQuantity는 전달한 회원의 장바구니 상품이 아닌 ID를 전달하면 예외가 발생한다.")
    void updateQuantityFailTest() {
        assertThatThrownBy(() -> cartItemService.updateQuantity(MEMBER_A, PIZZA.getId(), THREE_QUANTITY_REQUEST))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage("해당 사용자의 장바구니 상품이 아닙니다.");
    }

    @Test
    @DisplayName("remove는 회원과 삭제하고자 하는 장바구니 상품의 ID를 입력하면 해당 장바구니 상품을 삭제한다.")
    void removeSuccessTest() {
        assertDoesNotThrow(() -> cartItemService.remove(MEMBER_A, TWO_CHICKEN.getId()));
    }

    @Test
    @DisplayName("remove는 전달한 회원의 장바구니 상품이 아닌 ID를 전달하면 예외가 발생한다.")
    void removeFailTestWithForbiddenCartItem() {
        assertThatThrownBy(() -> cartItemService.remove(MEMBER_B, TWO_CHICKEN.getId()))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage("해당 사용자의 장바구니 상품이 아닙니다.");
    }

    @Test
    @DisplayName("remove는 존재하지 않는 장바구니 상품 ID를 전달하면 예외가 발생한다.")
    void removeFailTestWithNotExistCartItem() {
        assertThatThrownBy(() -> cartItemService.remove(MEMBER_A, -999L))
                .isInstanceOf(CartItemException.NotFound.class)
                .hasMessage("해당 장바구니 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("checkout은 회원의 장바구니 상품 중 선택한 장바구니 상품 ID들을 전달하면 선택한 상품들을 구매할 경우의 정보를 반환한다.")
    void checkoutSuccessTest() {
        List<Long> checkoutIds = List.of(TWO_CHICKEN.getId(), FOUR_SALAD.getId());
        CheckoutResponse actual = cartItemService.checkout(MEMBER_A, checkoutIds);

        int expectedTotalPrice = 30_000;
        int expectedEarnedPoints = 3_000;
        int expectedAvailablePoints = 3_000;
        int expectedChickenQuantity = 2;
        int expectedSaladQuantity = 4;

        assertAll(
                () -> assertThat(actual.getCartItems()).hasSize(2),
                () -> assertThat(actual.getCartItems().get(0).getQuantity()).isEqualTo(expectedChickenQuantity),
                () -> assertThat(actual.getCartItems().get(0).getProduct().getName()).isEqualTo(TWO_CHICKEN.getProduct().getName()),
                () -> assertThat(actual.getCartItems().get(1).getQuantity()).isEqualTo(expectedSaladQuantity),
                () -> assertThat(actual.getCartItems().get(1).getProduct().getName()).isEqualTo(FOUR_SALAD.getProduct().getName()),
                () -> assertThat(actual.getTotalPrice()).isEqualTo(expectedTotalPrice),
                () -> assertThat(actual.getCurrentPoints()).isEqualTo(MEMBER_A.getPoint()),
                () -> assertThat(actual.getEarnedPoints()).isEqualTo(expectedEarnedPoints),
                () -> assertThat(actual.getAvailablePoints()).isEqualTo(expectedAvailablePoints)
        );
    }
}
