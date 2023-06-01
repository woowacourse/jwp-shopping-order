package cart.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderDto;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.dto.OrderRequest;

@Service
public class OrderService {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;
    private final MemberService memberService;
    private final CouponService couponService;

    public OrderService(CartService cartService, CartItemService cartItemService, OrderItemDao orderItemDao,
            OrderDao orderDao, MemberService memberService, CouponService couponService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
        this.memberService = memberService;
        this.couponService = couponService;
    }

    @Transactional
    public void order(Member member, List<OrderRequest> requests) {
        Cart cart = cartService.getCartOf(member);

        List<CartItem> itemsToOrder = new ArrayList<>();
        for (OrderRequest request : requests) {
            CartItem item = cartItemService.getItemBy(request.getCartItemId());
            List<MemberCoupon> coupons = couponService.getMemberCouponsBy(member, request.getCouponIds());
            cart.applyCouponsOn(item, coupons);

            itemsToOrder.add(item);
        }
        Order order = cart.order(itemsToOrder);

        save(order);
        cartService.save(cart);
    }

    private void save(Order order) {
        Long orderId = orderDao.insert(OrderDto.of(order));
        orderItemDao.insertAll(orderId, order.getOrderItems());
    }

    public Order getBy(Long id) {
        OrderDto orderDto = orderDao.selectBy(id);
        return new Order(
                orderDto.getId(),
                memberService.getMemberBy(orderDto.getMemberId()),
                orderItemDao.selectAllOf(id),
                orderDto.getCreatedAt()
        );
    }

    public List<Order> getBy(Member member) {
        return orderDao.selectAllBy(member.getId()).stream()
                .map(OrderDto::getId)
                .map(this::getBy)
                .collect(Collectors.toList());
    }
}
