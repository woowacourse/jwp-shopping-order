package cart.application;

import cart.domain.cartItem.CartItem;
import cart.domain.cartItem.CartItemRepository;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.DiscountAction;
import cart.domain.member.Member;
import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.orderProduct.OrderProduct;
import cart.domain.product.Product;
import cart.dto.CouponResponse;
import cart.dto.ProductResponse;
import cart.dto.order.*;
import cart.util.ModelSortHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository, MemberCouponRepository memberCouponRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long addOrder(Member member, OrderRequest orderRequest) {
        List<OrderProduct> orderProducts = getOrderProducts(orderRequest.getSelectCartIds());
        Coupon usedCoupon = getUsedCoupon(orderRequest);
        int totalPrice = calculateTotalPriceOfProducts(orderProducts);
        int discountPrice = discountTotalPrice(usedCoupon, totalPrice);

        // TODO order가 MemberCoupon을 가지게 변경
        Order order = new Order(totalPrice, discountPrice, orderProducts, usedCoupon, member);
        validateUsableCoupon(usedCoupon, order);
        cartItemRepository.deleteByIds(orderRequest.getSelectCartIds());
        return orderRepository.add(order);
    }

    private Coupon getUsedCoupon(OrderRequest orderRequest) {
        if (orderRequest.getCouponId() == null) {
            return null;
        }
        MemberCoupon usedMemberCoupon = memberCouponRepository.findById(orderRequest.getCouponId());
        memberCouponRepository.delete(usedMemberCoupon.getId());
        return usedMemberCoupon.getCoupon();
    }

    private int discountTotalPrice(DiscountAction discountAction, int totalPrice) {
        if (discountAction == null) {
            return totalPrice;
        }
        int discountPrice = discountAction.discount(totalPrice);
        validatePaymentAmount(discountPrice);
        return discountPrice;
    }

    private void validatePaymentAmount(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("결제 금액은 0원보다 작을 수 없습니다.");
        }
    }

    private void validateUsableCoupon(Coupon usedCoupon, Order order) {
        if (usedCoupon != null) {
            if (!usedCoupon.isUsable(order)) {
                throw new IllegalArgumentException("해당 쿠폰은 사용할 수 없습니다.");
            }
        }
    }

    public List<OrderProduct> getOrderProducts(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartId -> {
                    CartItem cartItem = cartItemRepository.findById(cartId);
                    Product product = cartItem.getProduct();
                    return OrderProduct.of(product, cartItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public int calculateTotalPriceOfProducts(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity())
                .reduce(Integer::sum)
                .get();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersByMember(Member member) {
        List<Order> orders = orderRepository.findOrderByMemberId(member.getId());
        ModelSortHelper.sortByIdInDescending(orders);
        return orders.stream()
                .map(order -> new OrderResponse(order.getId(),
                        getOrderProductResponses(order.getOrderProducts()),
                        order.getConfirmState()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderById(Member member, Long id) {
        Order order = orderRepository.findOrderById(id);
        validateOwnerOfOrder(member, order);

        List<OrderProduct> orderProducts = order.getOrderProducts();
        ModelSortHelper.sortByIdInDescending(orderProducts);
        List<OrderProductResponse> orderProductResponses = getOrderProductResponses(orderProducts);
        CouponResponse couponResponse = getCouponResponse(order);
        return OrderDetailResponse.of(order, orderProductResponses, couponResponse);
    }

    public void deleteOrderById(Member member, Long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        validateOwnerOfOrder(member, order);
        validateConfirmBeforeDelete(order);
        reissueUsedCoupon(member, order);
        orderRepository.delete(orderId);
    }

    private void validateConfirmBeforeDelete(Order order) {
        if (order.isConfirm()) {
            throw new IllegalArgumentException("확정된 주문은 취소할 수 없습니다.");
        }
    }

    private void reissueUsedCoupon(Member member, Order order) {
        Coupon coupon = order.getUsedCoupon();
        if (coupon != null) {
            memberCouponRepository.add(new MemberCoupon(member, coupon));
        }
    }

    public OrderConfirmResponse confirmOrder(Member member, Long id) {
        Order order = orderRepository.findOrderById(id);
        validateOwnerOfOrder(member, order);
        order.confirm();
        orderRepository.update(order);

        Coupon bonusCoupon = issueBonusCoupon(member);
        CouponResponse couponResponse = CouponResponse.from(bonusCoupon);
        return new OrderConfirmResponse(couponResponse);
    }

    private Coupon issueBonusCoupon(Member member) { // TODO 보너스 쿠폰 지급 로직 구체화
        Coupon bonusCoupon = couponRepository.findById(1L);
        MemberCoupon bonusMemberCoupon = new MemberCoupon(member, bonusCoupon);
        memberCouponRepository.add(bonusMemberCoupon);
        return bonusCoupon;
    }

    private void validateOwnerOfOrder(Member member, Order order) {
        if (!order.getMember().equals(member)) {
            throw new IllegalArgumentException("접근할 수 없는 주문입니다.");
        }
    }

    private List<OrderProductResponse> getOrderProductResponses(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> new OrderProductResponse(
                        getProductResponse(orderProduct),
                        orderProduct.getQuantity())
                )
                .collect(Collectors.toList());
    }

    private static ProductResponse getProductResponse(OrderProduct orderProduct) {
        ProductResponse productResponse = new ProductResponse(
                orderProduct.getProduct().getId(),
                orderProduct.getName(),
                orderProduct.getPrice(),
                orderProduct.getImageUrl()
        );
        return productResponse;
    }

    private CouponResponse getCouponResponse(Order order) {
        Coupon coupon = order.getUsedCoupon();

        CouponResponse couponResponse = null;
        if (coupon != null) {
            couponResponse = CouponResponse.from(coupon);
        }
        return couponResponse;
    }
}
