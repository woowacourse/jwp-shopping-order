package cart.application;

import cart.db.repository.CouponRepository;
import cart.db.repository.OrderRepository;
import cart.db.repository.ProductRepository;
import cart.domain.product.Item;
import cart.domain.product.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.product.ItemRequest;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cart.exception.ErrorCode.INVALID_PRODUCT_ID;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CartItemService cartItemService;
    private final MemberCouponService memberCouponService;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    public OrderService(
            final CartItemService cartItemService, final MemberCouponService memberCouponService,
            final OrderRepository orderRepository,
            final CouponRepository couponRepository,
            final ProductRepository productRepository
    ) {
        this.cartItemService = cartItemService;
        this.memberCouponService = memberCouponService;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long add(final Member member, final OrderRequest orderRequest) {
        Map<Long, Integer> requestedItems = orderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ItemRequest::getProductId, ItemRequest::getQuantity));
        List<Long> productIds = new ArrayList<>(requestedItems.keySet());
        List<Product> products = productRepository.findByIds(productIds);
        validateOrderProductRequest(requestedItems, products);

        List<Item> items = products.stream()
                .map(product -> new Item(product, requestedItems.get(product.getId())))
                .collect(Collectors.toUnmodifiableList());

        Coupon coupon = null;
        if (orderRequest.getCouponId() != null) {
            coupon = couponRepository.findById(orderRequest.getCouponId());
            memberCouponService.useMemberCoupon(member, coupon.getId());
        }
        cartItemService.removeItemsByProductIds(member, productIds);
        Order order = new Order(member, items, coupon);
        return orderRepository.save(order);
    }

    public OrderResponse getOrder(final Member member, final Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);
        return new OrderResponse(order);
    }

    public List<OrderResponse> getOrders(final Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrderProductRequest(final Map<Long, Integer> orderRequestItems, final List<Product> products) {
        if (orderRequestItems.keySet().size() != products.size()) {
            throw new BadRequestException(INVALID_PRODUCT_ID);
        }
    }
}
