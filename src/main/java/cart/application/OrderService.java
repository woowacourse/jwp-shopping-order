package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductOrderDao;
import cart.domain.CartItems;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import cart.ui.dto.request.OrderProductRequest;
import cart.ui.dto.request.OrderRequest;
import cart.ui.dto.response.OrderListResponse;
import cart.ui.dto.response.OrderProductResponse;
import cart.ui.dto.response.OrderResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;
    private final ProductOrderDao productOrderDao;

    public OrderService(final OrderDao orderDao, final CouponDao couponDao, final CartItemDao cartItemDao,
        final ProductOrderDao productOrderDao) {
        this.orderDao = orderDao;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
        this.productOrderDao = productOrderDao;
    }

    @Transactional
    public OrderResponse order(final OrderRequest orderRequest, final Member member) {
        final CartItems cartItems = cartItemDao.findByMemberId(member.getId());
        if (cartItems.notContainsExactly(orderRequest.findProductIdAndQuantity())) {
            throw new BusinessException("장바구니에 존재하지 않거나 수량이 일치하지 않습니다.");
        }
        if (cartItems.isTotalAmountNotEquals(Amount.of(orderRequest.getTotalProductAmount()))) {
            throw new BusinessException("총 가격이 일치하지 않습니다.");
        }
        final Order order = makeOrder(orderRequest, member.getId(), cartItems);
        cartItemDao.deleteAll(cartItems);
        final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(orderRequest, cartItems);
        return new OrderResponse(order.getId(), orderRequest.getTotalProductAmount(),
            order.getDiscountedAmount().getValue(), order.getDeliveryAmount().getValue(), order.getAddress(),
            orderProductResponses);
    }

    private Order makeOrder(final OrderRequest orderRequest, final Long memberId, final CartItems cartItems) {
        if (orderRequest.getCouponId() == null) {
            return orderDao.save(
                new Order(cartItems.makeProductsReflectQuantity(), Amount.of(orderRequest.getTotalProductAmount()),
                    Amount.of(orderRequest.getTotalProductAmount()), Amount.of(orderRequest.getDeliveryAmount()),
                    orderRequest.getAddress()), memberId);
        }
        final Coupon coupon = couponDao.findByCouponIdAndMemberId(orderRequest.getCouponId(), memberId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
        final Coupon usedCoupon = coupon.use();
        couponDao.update(usedCoupon, memberId);
        return orderDao.save(
            new Order(cartItems.makeProductsReflectQuantity(), Amount.of(orderRequest.getTotalProductAmount()),
                coupon.calculateProduct(Amount.of(orderRequest.getTotalProductAmount())),
                Amount.of(orderRequest.getDeliveryAmount()), orderRequest.getAddress()), memberId);
    }

    private List<OrderProductResponse> makeOrderProductResponses(final OrderRequest orderRequest,
        final CartItems cartItems) {
        final List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        final List<Product> deduplicatedProducts = cartItems.getProducts()
            .getValue()
            .stream()
            .distinct()
            .collect(Collectors.toUnmodifiableList());
        for (int index = 0; index < deduplicatedProducts.size(); index++) {
            final Product product = deduplicatedProducts.get(index);
            final OrderProductRequest orderProductRequest = orderRequest.getProducts().get(index);
            final OrderProductResponse orderProductResponse = new OrderProductResponse(product.getId(),
                product.getName(), product.getAmount().getValue(),
                product.getImageUrl(), orderProductRequest.getQuantity());
            orderProductResponses.add(orderProductResponse);
        }
        return orderProductResponses;
    }

    public OrderResponse findOrder(final Long orderId) {
        final Order order = orderDao.findById(orderId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 주문입니다."));
        final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(order);
        return new OrderResponse(order.getId(), order.getTotalAmount().getValue(),
            order.getDiscountedAmount().getValue(), order.getDeliveryAmount().getValue(), order.getAddress(),
            orderProductResponses);
    }

    private List<OrderProductResponse> makeOrderProductResponses(final Order order) {
        final List<Product> products = order.getProducts().getValue();
        return products.stream()
            .map(it -> {
                final int quantity = productOrderDao.count(it.getId(), order.getId());
                return new OrderProductResponse(it.getId(), it.getName(), it.getAmount().getValue(), it.getImageUrl(),
                    quantity);
            })
            .collect(Collectors.toUnmodifiableList());
    }

    public List<OrderListResponse> findOrder(final Member member) {
        final List<Order> orders = orderDao.findByMember(member);
        return orders.stream()
            .map(order -> {
                final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(order);
                return new OrderListResponse(order.getId(), orderProductResponses);
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
