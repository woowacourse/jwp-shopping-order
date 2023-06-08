package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderHistoryDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemIdRequest;
import cart.dto.request.PaymentRequest;
import cart.exception.ErrorStatus;
import cart.exception.ShoppingOrderException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private OrderHistoryDao orderHistoryDao;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private MemberDao memberDao;

    @DisplayName("결제 요청시 회원의 포인트가 사용할 포인트보다 작다면 예외처리한다.")
    @Test
    void pay_fail_not_enough_point() {
        Member member = new Member(1L, "test", "123", 100);
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(1L)), 3_000, 1_000);

        assertThatThrownBy(() -> payService.pay(member, paymentRequest))
                .isInstanceOf(ShoppingOrderException.class)
                .hasMessage(ErrorStatus.POINT_NOT_ENOUGH.getMessage());
    }

    @DisplayName("결제 요청시 회원의 포인트가 사용할 포인트보다 작다면 예외처리한다.")
    @Test
    void pay_fail_point_over_price() {
        Member member = new Member(1L, "test", "123", 100);
        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(1L)), 3_000, 1_000);

        assertThatThrownBy(() -> payService.pay(member, paymentRequest))
                .isInstanceOf(ShoppingOrderException.class)
                .hasMessage(ErrorStatus.POINT_NOT_ENOUGH.getMessage());
    }

    @DisplayName("결제 요청시 상품 금액과 데이터 베이스의 금액이 다르면 예외 처리한다.")
    @Test
    void pay_fail_invalid_price() {
        Member member = new Member(1L, "test", "123", 100);

        Product product1 = new Product("product1", 1_000, "image1");
        Product product2 = new Product("product2", 2_000, "image1");

        CartItem cartItem1 = new CartItem(3, product1, member);
        CartItem cartItem2 = new CartItem(1, product2, member);

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        when(cartItemDao.findByIds(any())).thenReturn(cartItems);

        PaymentRequest paymentRequest = new PaymentRequest(List.of(new CartItemIdRequest(1L)), 1_000_000, 0);

        assertThatThrownBy(() -> payService.pay(member, paymentRequest))
                .isInstanceOf(ShoppingOrderException.class)
                .hasMessage(ErrorStatus.PAYMENT_PRICE_INVALID.getMessage());
    }

    @DisplayName("결제 요청에 성공하면 생성된 주문의 ID 응답값을 반환한다.")
    @Test
    void pay_success() {
        Member member = new Member("email", "password", 1_000);
        Product product1 = new Product("product1", 1_000, "image1");
        Product product2 = new Product("product2", 2_000, "image1");

        CartItem cartItem1 = new CartItem(1L, 3, product1, member);
        CartItem cartItem2 = new CartItem(2L, 1, product2, member);

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        List<CartItemIdRequest> cartItemIdRequests = List.of(new CartItemIdRequest(cartItem1.getId()),
                new CartItemIdRequest(cartItem2.getId()));

        PaymentRequest paymentRequest = new PaymentRequest(cartItemIdRequests, 5_000, 500);

        when(cartItemDao.findByIds(any())).thenReturn(cartItems);
        long orderHistoryId = 1L;
        when(orderHistoryDao.insert(any())).thenReturn(orderHistoryId);

        assertThat(payService.pay(member, paymentRequest).getOrderId()).isEqualTo(orderHistoryId);
    }
}