package cart.application;

import cart.domain.*;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(final CouponRepository couponRepository, final CartItemRepository cartItemRepository, final OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public Long order(final Member member, final OrderRequest request) {
        Coupon coupon = couponRepository.findById(request.getCouponId());

        List<CartItem> selectedCartItems = cartItemRepository.findByIds(request.getCartItemsIds());

        for (CartItem selectedCartItem : selectedCartItems) {
            selectedCartItem.checkOwner(member);
        }

        List<OrderItem> orderItems = selectedCartItems.stream()
                .map(CartItem::toOrderItem)
                .collect(Collectors.toList());
        Order order = Order.of(orderItems, member, coupon);
        return orderRepository.save(order);
    }
}
