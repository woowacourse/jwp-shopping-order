package cart.order.application;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponRepository;
import cart.order.application.dto.PlaceOrderCommand;
import cart.order.domain.service.OrderPlaceService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderPlaceService orderPlaceService;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;

    public OrderService(
            OrderPlaceService orderPlaceService,
            CartItemRepository cartItemRepository,
            CouponRepository couponRepository
    ) {
        this.orderPlaceService = orderPlaceService;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    public Long place(PlaceOrderCommand command) {
        List<CartItem> cartItems = command.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
        List<Coupon> coupons = couponRepository.findAllByIds(command.getCouponIds());
        return orderPlaceService.placeOrder(command.getMemberId(), cartItems, coupons);
    }
}
