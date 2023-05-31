package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.cartItem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.dto.order.OrderProductRequest;
import cart.dto.order.OrderRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderRepository orderRepository, final CouponDao couponDao, final CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional
    public Long createOrder(List<OrderRequest> orderRequests, Member member) {
        List<OrderItem> orderItems = requestToOrderItems(orderRequests, member);
        clearCartItems(orderRequests, member.getId());
        return orderRepository.create(orderItems, member.getId());
    }

    private List<OrderItem> requestToOrderItems(final List<OrderRequest> orderRequests, final Member member) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest orderRequest : orderRequests) {
            List<Coupon> coupons = couponDao.findById(orderRequest.getCouponIds());
            clearCoupons(coupons, member);

            OrderProductRequest productRequest = orderRequest.getProductRequest();
            orderItems.add(new OrderItem(productRequest.toProduct(), orderRequest.getQuantity(), coupons));
        }
        return orderItems;
    }

    private void clearCoupons(final List<Coupon> coupons, final Member member) {
        List<Coupon> memberCoupons = member.getCoupons();

        if (!memberCoupons.containsAll(coupons)) {
            throw new MemberCouponNotFoundException();
        }

        List<Long> couponIds = coupons.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        couponDao.delete(couponIds);
    }

    private void clearCartItems(final List<OrderRequest> orderRequests, final Long memberId) {
        List<Product> memberCartProducts = cartItemDao.findByMemberId(memberId)
                .stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList());

        List<Product> products = orderRequests.stream()
                .map(orderRequest -> orderRequest.getProductRequest().toProduct())
                .collect(Collectors.toList());

        if (!memberCartProducts.containsAll(products)) {
            throw new OrderCartMismatchException();
        }

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        cartItemDao.delete(memberId, productIds);
    }

    public List<Order> findAll(Long memberId) {
        return orderRepository.findAllByMemberId(memberId);
    }
}
