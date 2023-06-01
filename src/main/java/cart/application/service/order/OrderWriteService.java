package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import cart.domain.Member;
import cart.domain.discountpolicy.StorePaymentAmountPolicy;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderRequest;
import cart.ui.order.dto.DiscountRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderWriteService {
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    //config의존성주입 후 사용!
    private StorePaymentAmountPolicy storePaymentAmountPolicy;

    public Long createOrder(final Member member, final List<CreateOrderRequest> createOrderRequests, final DiscountRequest discountRequest) {
        //여기서 찾은 상품개수랑 처름 개수랑 다르면 예외

        //찾은 상품의 개수를 stream돌면서 수량 다르면 예외 처리
        return null;
    }

    public Long createOrder(final MemberAuth memberAuth, final CreateOrderRequest createOrderRequests) {
        return null;
    }
}
