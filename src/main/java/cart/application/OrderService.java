package cart.application;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.dto.CartItemDto;
import cart.dto.ProductDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderIdResponse;
import cart.dto.response.OrderResponse;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final DiscountPolicyProvider discountPolicyProvider;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(
            DiscountPolicyProvider discountPolicyProvider,
            MemberCouponRepository memberCouponRepository,
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository
    ) {
        this.discountPolicyProvider = discountPolicyProvider;
        this.memberCouponRepository = memberCouponRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public OrderIdResponse makeOrder(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItemsToOrder = getCartItemsToOrder(orderRequest, member);
        List<MemberCoupon> couponsToUse = getCouponsToUse(orderRequest);
        Money deliveryFee = new Money(orderRequest.getDeliveryFee());

        Order order = Order.make(cartItemsToOrder, couponsToUse, deliveryFee, member, discountPolicyProvider);

        Long orderId = orderRepository.insert(order);
        order.getAppliedCoupons().forEach(memberCouponRepository::updateCouponStatus);
        cartItemsToOrder.forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        return new OrderIdResponse(orderId);
    }

    private List<CartItem> getCartItemsToOrder(OrderRequest orderRequest, Member member) {
        List<CartItemDto> orderItemRequests = orderRequest.getCarItems();
        List<CartItem> cartItemsToOrder = orderItemRequests.stream()
                .map(CartItemDto::getId)
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());

        validateProductsRequestWithDatabase(orderItemRequests, cartItemsToOrder);

        cartItemsToOrder.forEach(cartItem -> cartItem.checkOwner(member));

        return cartItemsToOrder;
    }

    private void validateProductsRequestWithDatabase(List<CartItemDto> requestItems, List<CartItem> cartItemsToOrder) {
        validateProductsPrice(requestItems, cartItemsToOrder);
        validateProductsQuantity(requestItems, cartItemsToOrder);
    }

    private void validateProductsPrice(List<CartItemDto> orderItemRequests, List<CartItem> cartItemsToOrder) {
        for (int index = 0; index < orderItemRequests.size(); index++) {
            ProductDto requestProductInfo = orderItemRequests.get(index).getProduct();
            Product actualProductInfo = cartItemsToOrder.get(index).getProduct();
            validateProductPrice(requestProductInfo, actualProductInfo);
        }
    }

    private void validateProductPrice(ProductDto requestProductInfo, Product actualProductInfo) {
        if (requestProductInfo.getPrice() != actualProductInfo.getPrice()) {
            throw new OrderException.ProductPriceUpdated(actualProductInfo);
        }
    }

    private void validateProductsQuantity(List<CartItemDto> orderItemRequests, List<CartItem> cartItemsToOrder) {
        for (int index = 0; index < orderItemRequests.size(); index++) {
            CartItemDto requestCartItem = orderItemRequests.get(index);
            CartItem actualCartItem = cartItemsToOrder.get(index);
            validateProductQuantity(requestCartItem, actualCartItem);
        }
    }

    private void validateProductQuantity(CartItemDto requestCartItem, CartItem actualCartItem) {
        if (requestCartItem.getQuantity() != actualCartItem.getQuantity()) {
            throw new OrderException.QuantityNotMatched(actualCartItem);
        }
    }

    private List<MemberCoupon> getCouponsToUse(OrderRequest orderRequest) {
        return orderRequest.getCouponIds().stream()
                .map(memberCouponRepository::findByIdForUpdate)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse findOrderInfo(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);
        return OrderResponse.of(order);
    }
}
