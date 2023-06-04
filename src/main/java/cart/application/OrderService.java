package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.dto.request.OrderPostRequest;
import cart.dto.response.OrderPreviewResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.ProductInOrderResponse;
import cart.exception.AuthenticationException;
import cart.exception.NoSuchDataExistException;
import cart.exception.UnauthorizedAccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final CartItemDao cartItemDao;
    private final DiscountPolicy discountPolicy;

    public OrderService(final OrderDao orderDao, final OrderProductDao orderProductDao, final CartItemDao cartItemDao,
                        final DiscountPolicy discountPolicy) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.cartItemDao = cartItemDao;
        this.discountPolicy = discountPolicy;
    }

    public OrderResponse findOrderById(final Member member, final Long orderId) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(NoSuchDataExistException::new);
        if (!order.getMember().matchMemberByInfo(member)) {
            throw new AuthenticationException("해당 주문에 접근 할 권한이 없습니다.");
        }

        final List<OrderProduct> orderProducts = orderProductDao.findByOrderId(orderId);

        final List<ProductInOrderResponse> productInOrderResponses = orderProducts.stream()
                .map(this::toProductInOrderResponse)
                .collect(Collectors.toList());
        return toOrderResponse(order, productInOrderResponses);
    }

    public List<OrderPreviewResponse> findAllOrdersByMember(final Member member) {
        final List<OrderProduct> byMemberId = orderProductDao.findByMemberId(member.getId());
        final Map<Order, List<OrderProduct>> ordersWithProduct = byMemberId
                .stream()
                .collect(Collectors.groupingBy(OrderProduct::getOrder));

        final List<OrderPreviewResponse> results = new ArrayList<>();
        for (final Order order : ordersWithProduct.keySet()) {
            final List<OrderProduct> orderProducts = ordersWithProduct.get(order);
            final Product mainProduct = orderProducts.get(0).getProduct();
            results.add(toOrderPreviewResponse(order, orderProducts.size() - 1, mainProduct));
        }

        return results;
    }

    private ProductInOrderResponse toProductInOrderResponse(final OrderProduct orderProduct) {
        final Product product = orderProduct.getProduct();
        return new ProductInOrderResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                orderProduct.getQuantity(),
                product.getImageUrl()
        );
    }

    private OrderResponse toOrderResponse(final Order order, final List<ProductInOrderResponse> productsResponses) {
        return new OrderResponse(order.getTotalPrice(), order.getFinalPrice(), order.getCreatedAt(), productsResponses);
    }

    private OrderPreviewResponse toOrderPreviewResponse(
            final Order order,
            final int extraProductCount,
            final Product mainProduct
    ) {
        return new OrderPreviewResponse(
                order.getId(),
                mainProduct.getName(),
                mainProduct.getImageUrl(),
                extraProductCount,
                order.getCreatedAt(),
                order.getFinalPrice()
        );
    }

    @Transactional
    public Long addOrder(final Member member, final OrderPostRequest request) {
        final List<CartItem> cartItemsByIds = cartItemDao.findByIds(request.getCartItems());
        validateIsMemberCartItems(member, cartItemsByIds);
        final Order order = createOrder(member, cartItemsByIds);
        validateFinalPrice(order.getFinalPrice(), request.getFinalPrice());

        final List<OrderProduct> orderProducts = createOrderProducts(cartItemsByIds, order);
        orderProductDao.saveOrderProducts(orderProducts);
        cartItemDao.deleteByIds(collectCartItemIds(cartItemsByIds));

        return order.getId();
    }

    private void validateIsMemberCartItems(final Member member, final List<CartItem> cartItems) {
        final boolean isMembersCartItem = cartItems.stream()
                .map(CartItem::getMember)
                .allMatch(member::matchMemberByInfo);

        if (!isMembersCartItem) {
            throw new UnauthorizedAccessException();
        }
    }

    private Order createOrder(final Member member, final List<CartItem> cartItemsByIds) {
        final int totalPrice = cartItemsByIds.stream()
                .mapToInt(CartItem::calculateTotalPrice)
                .sum();

        final int finalPrice = discountPolicy.discountAmountByPrice(totalPrice);
        final Long savedOrderId = orderDao.saveOrder(new Order(member, totalPrice, finalPrice));
        return orderDao.findById(savedOrderId)
                .orElseThrow(NoSuchDataExistException::new);
    }

    private void validateFinalPrice(final int finalPrice, final int requestPrice) {
        if (finalPrice != requestPrice) {
            throw new IllegalStateException("주문 최종 금액과 해당 금액이 일치하지 않습니다.");
        }
    }

    private List<OrderProduct> createOrderProducts(final List<CartItem> cartItemsByIds, final Order order) {
        return cartItemsByIds.stream()
                .map(cartItem -> new OrderProduct(order, cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<Long> collectCartItemIds(final List<CartItem> cartItemsByIds) {
        return cartItemsByIds.stream().map(CartItem::getId).collect(Collectors.toList());
    }
}
