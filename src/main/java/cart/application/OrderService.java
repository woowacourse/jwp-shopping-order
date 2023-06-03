package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.dao.ProductOrderDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.dto.CartItemRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;
    private final ProductOrderDao productOrderDao;

    public OrderService(final OrderDao orderDao, final ProductDao productDao, final CouponDao couponDao,
        final CartItemDao cartItemDao, final ProductOrderDao productOrderDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
        this.productOrderDao = productOrderDao;
    }

    public OrderResponse order(final OrderRequest orderRequest, final Member member) {
        final List<Product> products = findProducts(orderRequest);
        final Coupon coupon = couponDao.findByCouponIdAndMemberId(orderRequest.getCouponId(), member.getId())
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
        final Order order = orderDao.save(
            new Order(new Products(products), coupon, Amount.of(orderRequest.getDeliveryAmount()),
                Amount.of(orderRequest.getTotalProductAmount()), orderRequest.getAddress()), member.getId());
        final Coupon usedCoupon = coupon.use();
        couponDao.update(usedCoupon, member.getId());
        deleteCartItem(orderRequest, member.getId());
        final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(orderRequest,
            products);
        return new OrderResponse(order.getId(), orderRequest.getTotalProductAmount(),
            order.getDeliveryAmount().getValue(), order.discountProductAmount().getValue(), order.getAddress(),
            orderProductResponses);
    }

    private List<Product> findProducts(final OrderRequest orderRequest) {
        final List<Product> products = new ArrayList<>();
        final List<Amount> amounts = orderRequest.getProducts()
            .stream()
            .map(it -> {
                final Product product = productDao.getProductById(it.getProductId());
                addToProducts(products, it.getQuantity(), product);
                return product.getAmount().multiply(it.getQuantity());
            })
            .collect(Collectors.toList());
        checkTotalAmount(Amount.of(orderRequest.getTotalProductAmount()), amounts);
        return products;
    }

    private void addToProducts(final List<Product> products, final int quantity, final Product product) {
        for (int count = 0; count < quantity; count++) {
            products.add(product);
        }
    }

    private void checkTotalAmount(final Amount totalAmount, final List<Amount> amounts) {
        final Amount sum = Amount.of(amounts);
        if (!sum.equals(totalAmount)) {
            throw new BusinessException("상품 금액이 변경되었습니다.");
        }
    }

    private List<OrderProductResponse> makeOrderProductResponses(final OrderRequest orderRequest,
        final List<Product> products) {
        final List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        final List<Product> deduplicatedProducts = products.stream()
            .distinct()
            .collect(Collectors.toUnmodifiableList());
        for (int index = 0; index < deduplicatedProducts.size(); index++) {
            final Product product = deduplicatedProducts.get(index);
            final CartItemRequest cartItemRequest = orderRequest.getProducts().get(index);
            final OrderProductResponse orderProductResponse = new OrderProductResponse(product.getId(),
                product.getName(), product.getAmount().getValue(),
                product.getImageUrl(), cartItemRequest.getQuantity());
            orderProductResponses.add(orderProductResponse);
        }
        return orderProductResponses;
    }

    private void deleteCartItem(final OrderRequest orderRequest, final Long memberId) {
        for (final CartItemRequest product : orderRequest.getProducts()) {
            cartItemDao.delete(memberId, product.getProductId());
        }
    }

    public OrderResponse findOrder(final Long orderId) {
        final Order order = orderDao.findById(orderId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 주문입니다."));
        final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(order);
        return new OrderResponse(order.getId(), order.getTotalAmount().getValue(),
            order.getDeliveryAmount().getValue(), order.discountProductAmount().getValue(), order.getAddress(),
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
}
