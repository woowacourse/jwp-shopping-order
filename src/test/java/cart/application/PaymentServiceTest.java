package cart.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.application.Event.RequestPaymentEvent;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Quantity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.point.Point;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.dto.request.OrderRequest;
import cart.exception.PointException;
import cart.repository.PointRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(pointRepository);
    }

    @Test
    void 결제시_보유_포인트보다_많은_포인트_사용을_요청하면_예외가_발생한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final OrderRequest orderRequest = new OrderRequest(List.of(1L), 100);
        final Product product = new Product(1L, new Name("상품"), new ImageUrl("img.com"), new Price(1000));
        final Cart cart = new Cart(List.of(new CartItem(1L, new Quantity(2), product, member)));
        final RequestPaymentEvent requestPaymentEvent = new RequestPaymentEvent(
                member,
                cart.getTotalPrice(),
                orderRequest
        );
        final Point memberPoint = new Point(0);
        given(pointRepository.findPointByMember(member)).willReturn(memberPoint);

        // expect
        assertThatThrownBy(() -> paymentService.pay(requestPaymentEvent))
                .isInstanceOf(PointException.BadRequest.class)
                .hasMessage("멤버가 소유한 포인트 (" + memberPoint.getPoint() + ") 보다 더 많은 포인트를 사용할 수 없습니다.");
    }

    @Test
    void 결제시_상품_가격보다_많은_포인트_사용을_요청하면_예외가_발생한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final OrderRequest orderRequest = new OrderRequest(List.of(1L), 10001);
        final Product product = new Product(1L, new Name("상품"), new ImageUrl("img.com"), new Price(1000));
        final Cart cart = new Cart(List.of(new CartItem(1L, new Quantity(2), product, member)));
        final RequestPaymentEvent requestPaymentEvent = new RequestPaymentEvent(
                member,
                cart.getTotalPrice(),
                orderRequest
        );
        final Point memberPoint = new Point(1000000);
        given(pointRepository.findPointByMember(member)).willReturn(memberPoint);

        // expect
        assertThatThrownBy(() -> paymentService.pay(requestPaymentEvent))
                .isInstanceOf(PointException.BadRequest.class)
                .hasMessage("총 구매 가격보다 (2000) 더 많은 포인트는 사용할 수 없습니다.");
    }
}
