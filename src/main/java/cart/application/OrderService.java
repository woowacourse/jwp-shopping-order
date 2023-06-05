package cart.application;

import cart.domain.cartItem.CartItem;
import cart.domain.cartItem.CartItemRepository;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
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
        int totalPrice = calculateTotalPriceOfProducts(orderProducts);
        int discountPrice = totalPrice;
        Coupon usedCoupon = null;
        if (orderRequest.getCouponId() != null) {
            MemberCoupon usedMemberCoupon = memberCouponRepository.findById(orderRequest.getCouponId());
            discountPrice = usedMemberCoupon.discount(totalPrice);
            memberCouponRepository.delete(usedMemberCoupon.getId());
            usedCoupon = usedMemberCoupon.getCoupon();

            validatePaymentAmount(discountPrice);
        }

        // TODO order가 MemberCoupon을 가지게 변경
        Order order = new Order(totalPrice, discountPrice, orderProducts, usedCoupon, member);
        validateUsableCoupon(usedCoupon, order);
        orderRequest.getSelectCartIds().stream()
                .forEach(cartItemRepository::deleteById);
        return orderRepository.add(order);
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
                    return new OrderProduct(
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl(),
                            cartItem.getQuantity(),
                            product);
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
                .map(order -> {
                    List<OrderProductResponse> orderProductResponses = order.getOrderProducts().stream()
                            .map(orderProduct -> {
                                ProductResponse productResponse = new ProductResponse(
                                        orderProduct.getProduct().getId(),
                                        orderProduct.getName(),
                                        orderProduct.getPrice(),
                                        orderProduct.getImageUrl()
                                );

                                return new OrderProductResponse(productResponse, orderProduct.getQuantity());
                            }).collect(Collectors.toList());

                    return new OrderResponse(order.getId(), orderProductResponses, order.getConfirmState());
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderById(Member member, Long id) {
        Order order = orderRepository.findOrderById(id);
        validateOwnerOfOrder(member, order);

        List<OrderProduct> orderProducts = order.getOrderProducts();
        ModelSortHelper.sortByIdInDescending(orderProducts);
        List<OrderProductResponse> orderProductResponses = orderProducts.stream()
                .map(orderProduct -> {
                    ProductResponse productResponse = new ProductResponse(
                            orderProduct.getProduct().getId(),
                            orderProduct.getName(),
                            orderProduct.getPrice(),
                            orderProduct.getImageUrl()
                    );

                    return new OrderProductResponse(
                            productResponse,
                            orderProduct.getQuantity()
                    );
                }).collect(Collectors.toList());

        Coupon coupon = order.getUsedCoupon();

        CouponResponse couponResponse = null;
        if (coupon != null) {
            couponResponse = CouponResponse.from(coupon);
        }

        return new OrderDetailResponse( // originalPrice, discountPrice, confirmState, couponResponse
                order.getId(),
                orderProductResponses,
                order.getOriginalPrice(),
                order.getDiscountPrice(),
                order.getConfirmState(),
                couponResponse
        );
    }

    public void deleteOrderById(Member member, Long orderId) {
        // 유저의 order인지 검증
        Order order = orderRepository.findOrderById(orderId);
        validateOwnerOfOrder(member, order);

        // 확정되지 않은 주문인지 검증
        if (order.isConfirm()) {
            throw new IllegalArgumentException("확정된 주문은 취소할 수 없습니다.");
        }

        Coupon coupon = order.getUsedCoupon();
        orderRepository.delete(orderId);

        // 사용했던 쿠폰 재발급
        if (coupon != null) {
            memberCouponRepository.add(new MemberCoupon(member, coupon));
        }
    }

    public OrderConfirmResponse confirmOrder(Member member, Long id) {
        Order order = orderRepository.findOrderById(id);
        validateOwnerOfOrder(member, order);
        order.confirm();
        orderRepository.update(order);

        // TODO 보너스 쿠폰 지급 로직 추가
        Coupon bonusCoupon = couponRepository.findById(1L);
        MemberCoupon bonusMemberCoupon = new MemberCoupon(member, bonusCoupon);
        memberCouponRepository.add(bonusMemberCoupon);

        CouponResponse couponResponse = CouponResponse.from(bonusCoupon);
        return new OrderConfirmResponse(couponResponse);
    }

    private void validateOwnerOfOrder(Member member, Order order) {
        if (!order.getMember().equals(member)) {
            throw new IllegalArgumentException("접근할 수 없는 주문입니다.");
        }
    }
}
