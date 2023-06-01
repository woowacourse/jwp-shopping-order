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
        MemberCoupon usedMemberCoupon = null;
        if (orderRequest.getCouponId() != null) {
            usedMemberCoupon = memberCouponRepository.findById(orderRequest.getCouponId());
            discountPrice = usedMemberCoupon.discount(totalPrice);
            memberCouponRepository.delete(usedMemberCoupon.getId());
        }

        // TODO order가 MemberCoupon을 가지게 변경
        Order order = new Order(totalPrice, discountPrice, orderProducts, usedMemberCoupon.getCoupon(), member);
        orderRequest.getSelectCartIds().stream()
                .forEach(cartItemDao::deleteById);
        return orderRepository.add(order);
    }

    public List<OrderProduct> getOrderProducts(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartId -> {
                    CartItem cartItem = cartItemDao.findById(cartId);
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
    public OrderDetailResponse findOrderById(Long id) {
        Order order = orderRepository.findOrderById(id);
        List<OrderProductResponse> orderProductResponses = order.getOrderProducts().stream()
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
        CouponResponse couponResponse = new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountType().getName(),
                coupon.getDiscountPercent(),
                coupon.getDiscountAmount(),
                coupon.getMinimumPrice()
        );

        return new OrderDetailResponse( // originalPrice, discountPrice, confirmState, couponResponse
                order.getId(),
                orderProductResponses,
                order.getOriginalPrice(),
                order.getDiscountPrice(),
                order.getConfirmState(),
                couponResponse
        );
    }

    public void deleteOrderById(Member member, Long id) {
        Coupon coupon = orderRepository.findOrderById(id).getUsedCoupon();
        orderRepository.delete(id);

        // 사용했던 쿠폰 재발급
        memberCouponRepository.add(new MemberCoupon(member, coupon));
    }

    public OrderConfirmResponse confirmOrder(Long id) {
        Order order = orderRepository.findOrderById(id);
        order.confirm();
        orderRepository.update(order);

        // 사용 가능한 쿠폰을 유저에게 발급
        // response null 테스트해보기
        // 임시로 id가 1인 쿠폰 반환
        Coupon coupon = couponRepository.findById(1L);
        CouponResponse couponResponse = new CouponResponse(coupon.getId(), coupon.getName(), coupon.getDiscountType().getName(), coupon.getDiscountPercent(), coupon.getDiscountAmount(), coupon.getMinimumPrice());
        return new OrderConfirmResponse(couponResponse);
    }


}

/*

 * [ ] 주문 확정
 * [ ] 주문 확정시 사용 가능한 쿠폰을 유저에게 발급

 * [ ] 주문 리스트 조회
 * [ ] 주문 상세 조회

 * [ ] 주문 취소
 * [ ] 주문시 사용했던 쿠폰 재발급
 */
