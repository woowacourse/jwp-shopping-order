package cart.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderItemDto;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.MemberCouponRequest;
import cart.dto.OrderItemRequest;
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
    public void order(Member member, OrderRequest request) {
        Cart cart = cartService.getCartOf(member);

        List<CartItem> itemsToOrder = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            CartItem item = cartItemService.getItemByProductId(orderItemRequest.getProduct().getId());
            List<Long> couponIds = orderItemRequest.getCoupons()
                    .stream()
                    .map(MemberCouponRequest::getCouponId)
                    .collect(Collectors.toList());
            List<MemberCoupon> coupons = couponService.getMemberCouponsBy(member, couponIds);
            cart.applyCouponsOn(item, coupons);

            itemsToOrder.add(item);
        }
        Order order = cart.order(itemsToOrder);

        save(order);
        cartService.save(cart);
    }

    public List<Order> getBy(Member owner) {
        return orderDao.selectAllBy(owner.getId()).stream()
                .map(OrderDto::getId)
                .map(id -> getBy(owner, id))
                .collect(Collectors.toList());
    }

    public Order getBy(Member owner, Long id) {
        OrderDto orderDto = orderDao.selectBy(id);
        return new Order(
                orderDto.getId(),
                memberService.getMemberBy(orderDto.getMemberId()),
                toOrderItems(owner, orderItemDao.selectAllOf(id)),
                orderDto.getCreatedAt()
        );
    }

    private void save(Order order) {
        Long orderId = orderDao.insert(OrderDto.of(order));
        orderItemDao.insertAll(orderId, order.getOrderItems());
    }

    private List<OrderItem> toOrderItems(Member owner, List<OrderItemDto> orderItemDtos) {
        return orderItemDtos.stream()
                .map(dto -> toOrderItem(owner, dto))
                .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(Member owner, OrderItemDto orderItemDto) {
        return new OrderItem(
                orderItemDto.getId(),
                new Product(
                        orderItemDto.getOrderedProductId(),
                        orderItemDto.getOrderedProductName(),
                        orderItemDto.getOrderedProductPrice(),
                        orderItemDto.getOrderedProductImageUrl()
                ),
                orderItemDto.getQuantity(),
                couponService.getMemberCouponsBy(owner, orderItemDto.getMemberCouponIds())
        );
    }
}
