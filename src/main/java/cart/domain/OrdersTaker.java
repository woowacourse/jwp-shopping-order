package cart.domain;

import cart.dto.OrdersResponse;
import cart.exception.OrdersPriceNotMatchException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrdersRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdersTaker {
    private final OrdersRepository ordersRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    public OrdersTaker(OrdersRepository ordersRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository, ProductRepository productRepository) {
        this.ordersRepository = ordersRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.productRepository = productRepository;
    }

    public long takeOrder(final long memberId, final List<Long> cartIds, final List<Long> coupons) {
        final int originalPrice = calculateCartItemsOriginalPrice(cartIds);
        final int discountPrice = calculateDiscountPrice(memberId, originalPrice, coupons);
        final long orderId = ordersRepository.takeOrders(memberId, discountPrice);
        List<ProductQuantity> productIdQuantity = cartItemRepository.changeCartItemToOrdersItemAndGetProductQuantities(cartIds);
        ordersRepository.createOrdersCartItems(orderId,productIdQuantity);
        couponRepository.addOrdersCoupon(orderId, coupons);
        return orderId;
    }

    private int calculateCartItemsOriginalPrice(final List<Long> cartIds) {
        return cartIds.stream()
                .map(cartItemRepository::findTotalPriceByCartId)
                .reduce(0, Integer::sum);
    }

    private int calculateOrdersItemOriginalPrice(final long orderId) {
        return productRepository.findProductIdQuantityWithOrdersId(orderId);
    }

    private int calculateDiscountPrice(final long memberId, final int originalPrice, final List<Long> couponIds) {
        int discountPrice = originalPrice;
        List<Coupon> coupons = couponIds.stream()
                .map(couponRepository::findById)
                .collect(Collectors.toList());
        for (Coupon coupon : coupons) {
            couponRepository.withDrawCouponWithId(memberId, coupon);
            discountPrice = (int) ((1 - coupon.getDiscountRate()) * discountPrice) - coupon.getDiscountAmount();
            validateLimit(originalPrice, coupon.getMinimumPrice());
        }
        validateNegativePrice(discountPrice);
        return discountPrice;
    }

    public List<OrdersResponse> findOrdersWithMember(final Member member) {
        return reverse(ordersRepository.findAllOrdersByMember(member).stream()
                .map(orders -> makeResponse(member, Optional.ofNullable(orders)))
                .collect(Collectors.toList()));
    }

    public OrdersResponse findOrdersWithId(final Member member, final long id) {
        return makeResponse(member, ordersRepository.findOrdersById(id));
    }

    private OrdersResponse makeResponse(final Member member, final Optional<Orders> orders) {
        if (orders.isEmpty()) {
            return OrdersResponse.noOrdersMembersResponse();
        }
        List<CartItem> ordersItem = ordersRepository.findCartItemByOrdersIds(member, orders.get().getId());
        List<Coupon> coupons = couponRepository.findByOrdersId(orders.get().getId());
        return OrdersResponse.of(
                orders.get(),
                ordersItem,
                calculateOrdersItemOriginalPrice(orders.get().getId()),
                coupons
        );
    }

    private void validateLimit(final int price, final int limit) {
        if (price < limit) {
            throw new OrdersPriceNotMatchException(limit + "이상으로 구매하셔야 합니다. ( 현재금액 : " + price + ")");
        }
    }

    private void validateNegativePrice(final int price) {
        if (price < 0) {
            throw new OrdersPriceNotMatchException("금액은 음수가 될 수 없습니다.");
        }
    }

    private List<OrdersResponse> reverse(List<OrdersResponse> responses) {
        Collections.reverse(responses);
        return responses;
    }
}
