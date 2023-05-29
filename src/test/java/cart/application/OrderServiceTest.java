package cart.application;

import static cart.fixtures.CartItemFixtures.맥북_ID_5_1개_1500000원;
import static cart.fixtures.CartItemFixtures.바닐라_크림_콜드브루_ID_4_3개_17400원;
import static cart.fixtures.CartItemFixtures.유자_민트_티_ID_1_5개_29500원;
import static cart.fixtures.CartItemFixtures.자몽_허니_블랙티_ID_2_7개_39900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.repository.OrderRepository;
import cart.repository.dao.CartItemDao;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private final CartItemDao cartItemDao = mock(CartItemDao.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    private final OrderService orderService = new OrderService(cartItemDao, orderRepository);

    private final Member member = new Member(1L, "test@email.com", "password");

    @BeforeEach
    void setUp() {
        List<CartItem> cartItems = List.of(
                유자_민트_티_ID_1_5개_29500원(member),
                자몽_허니_블랙티_ID_2_7개_39900원(member),
                맥북_ID_5_1개_1500000원(member));

        willReturn(cartItems).given(cartItemDao).findByIds(List.of(1L, 2L, 5L));
        willReturn(1L).given(orderRepository).createOrder(any());
    }

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(List.of(1L, 2L, 5L), 1_557_400);

        // when
        final Long saveId = orderService.order(member, orderCreateRequest);

        // then
        assertThat(saveId).isEqualTo(1L);
    }

    @DisplayName("주문 상품 중, 주문자의 장바구니에 담기지 않은 상품이 포함되어 있으면 예외가 발생한다")
    @Test
    void createOrder_containsOtherMemberCartItem_throws() {
        // given
        final Member otherMember = new Member(2L, "other@email.com", "password");
        final List<CartItem> illegalCartItems = List.of(
                유자_민트_티_ID_1_5개_29500원(member),
                자몽_허니_블랙티_ID_2_7개_39900원(member),
                바닐라_크림_콜드브루_ID_4_3개_17400원(otherMember)
        );
        willReturn(illegalCartItems).given(cartItemDao).findByIds(List.of(1L, 2L, 4L));

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(List.of(1L, 2L, 4L), 84_800);

        // when, then
        assertThatThrownBy(() -> orderService.order(member, orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 Member의 장바구니 상품이 포함되어 있습니다");
    }

    @DisplayName("주문하려는 상품이 존재하지 않으면 예외가 발생한다")
    @Test
    void createOrder_emptyOrderItem_throws() {
        // given
        final List<Long> nonExistIds = List.of(Long.MAX_VALUE, Long.MAX_VALUE - 1);
        final List<CartItem> illegalCartItems = Collections.emptyList();
        willReturn(illegalCartItems).given(cartItemDao).findByIds(nonExistIds);

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(nonExistIds, 84_800);

        // when, then
        assertThatThrownBy(() -> orderService.order(member, orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니 상품이 존재하지 않습니다");
    }

    @DisplayName("클라이언트에서 계산한 총 금액과 서버에서 계산한 총 금액이 일치하지 않으면 예외가 발생한다")
    @Test
    void createOrder_differentPriceBetweenServerAndClient_throws() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(List.of(1L, 2L, 5L), Integer.MAX_VALUE);

        // when, then
        assertThatThrownBy(() -> orderService.order(member, orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산된 금액이 일치하지 않습니다");
    }
}
