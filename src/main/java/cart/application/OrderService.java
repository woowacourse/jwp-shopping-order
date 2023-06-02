package cart.application;

import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.dto.CouponResponse;
import cart.dto.OrderConfirmResponse;
import cart.dto.ProductResponse;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderProductResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.persistence.dao.CartItemDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemDao cartItemDao; // TODO repository 생성
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(OrderRepository orderRepository, CartItemDao cartItemDao, CouponRepository couponRepository, MemberCouponRepository memberCouponRepository) {
        this.orderRepository = orderRepository;
        this.cartItemDao = cartItemDao;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    // 주문 신청
    public Long addOrder(Member member, OrderRequest orderRequest) { // TODO usecase로 분리
        // TODO selectCartId & couponId가 존재하지 않을 경우 예외 처리
        // 주문한 장바구니 상품 리스트 조회
        List<OrderProduct> orderProducts = getOrderProducts(orderRequest.getSelectCartIds());

        // 총가격, 할인가 계산
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
                .forEach(cartItemDao::deleteById);
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
                    CartItem cartItem = cartItemDao.findById(cartId).orElseThrow(() -> new IllegalArgumentException("접근할 수 없는 장바구니 아이템입니다."));
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
            couponResponse = new CouponResponse(
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountType().getName(),
                    coupon.getDiscountPercent(),
                    coupon.getDiscountAmount(),
                    coupon.getMinimumPrice()
            );
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

        CouponResponse couponResponse = new CouponResponse(bonusCoupon.getId(), bonusCoupon.getName(), bonusCoupon.getDiscountType().getName(), bonusCoupon.getDiscountPercent(), bonusCoupon.getDiscountAmount(), bonusCoupon.getMinimumPrice());
        return new OrderConfirmResponse(couponResponse);
    }

    private void validateOwnerOfOrder(Member member, Order order) {
        if (!order.getMember().equals(member)) {
            throw new IllegalArgumentException("접근할 수 없는 주문입니다.");
        }
    }
}
