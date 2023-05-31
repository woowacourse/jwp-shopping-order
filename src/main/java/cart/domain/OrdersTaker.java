package cart.domain;

import cart.dto.OrdersResponse;
import cart.exception.OrdersPriceNotMatchException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrdersRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    public long takeOrder(final long memberId, final List<Long> cartIds, final List<Long> coupons){
        final int originalPrice = calculateOriginalPrice(cartIds);
        final int discountPrice = calculateDiscountPrice(originalPrice,coupons);
        final long orderId = ordersRepository.takeOrders(memberId,discountPrice);
        cartItemRepository.changeCartItemToOrdersItem(orderId,cartIds);
        couponRepository.addOrdersCoupon(orderId,coupons);
        return orderId;
    }
    private int calculateOriginalPrice(final List<Long> cartIds){
        return   cartItemRepository.findProductQuantityByCartItemIds(cartIds).entrySet().stream()
                .map(entry -> productRepository.getPriceById(entry.getKey())*entry.getValue())
                .reduce(0,Integer::sum);
    }
    private int calculateDiscountPrice(final int originalPrice, final List<Long> couponIds){
        int discountPrice = originalPrice;
        List<Coupon> coupons = couponRepository.findCouponsById(couponIds);
        for(Coupon coupon: coupons){
            discountPrice = (int) ((1-coupon.getDiscountRate()) * discountPrice) + coupon.getDiscountAmount();
            validateLimit(originalPrice,coupon.getMinimumPrice());
        }
        return discountPrice;
    }
    public List<OrdersResponse> findOrdersWithOriginalPrice(final Member member){
        return ordersRepository.findAllOrdersByMember(member).stream()
                .map(orders -> makeResponse(member,orders))
                .collect(Collectors.toList());
    }
    public OrdersResponse findOrdersWithId(final Member member,final long id){
        Orders orders = ordersRepository.findOrdersById(member,id);
        return makeResponse(member,orders);
    }
    private OrdersResponse makeResponse(final Member member,final Orders orders){
        List<CartItem> cartItems = cartItemRepository.findCartItemsByOrderId(member,orders.getId());
        List<Long> cartItemsId = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        List<Coupon> coupons = couponRepository.findByOrdersId(orders.getId());
        return OrdersResponse.of(
                orders,
                cartItems,
                calculateOriginalPrice(cartItemsId),
                coupons
        );
    }
    private void validateLimit(final int price, final int limit){
        if(price<limit){
            throw new OrdersPriceNotMatchException( limit + "이상으로 구매하셔야 합니다. ( 현재금액 : " + price +")");
        }
    }
}
