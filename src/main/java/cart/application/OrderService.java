package cart.application;

import cart.db.repository.CouponRepository;
import cart.db.repository.OrderRepository;
import cart.db.repository.ProductRepository;
import cart.domain.Item;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import cart.dto.ItemRequest;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    public OrderService(final OrderRepository orderRepository, final CouponRepository couponRepository, final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void add(final Long memberId, final OrderRequest orderRequest) {
        Map<Long, Integer> orderRequestItems = orderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ItemRequest::getProductId, ItemRequest::getQuantity));

        List<Product> products = productRepository.findByIds(new ArrayList<>(orderRequestItems.keySet()));
        // TODO: 유효성 검증: 개수가 일치하는지 확인

        List<Item> items = products.stream()
                .map(product -> new Item(product, orderRequestItems.get(product.getId())))
                .collect(Collectors.toUnmodifiableList());

        Long couponId = orderRequest.getCouponId();
        Coupon coupon = couponRepository.findById(couponId); // 쿠폰 ID 유효성 검사
        Order order = new Order(items, coupon, 10000, 3000);

        Long orderId = orderRepository.addOrder(memberId, order);
        orderRepository.addOrderProducts(orderId, items);
        orderRepository.addOrderCoupon(orderId, couponId);
    }

    public OrderResponse findOrdersByIdAndMemberId(final Long memberId, final Long orderId) {
        Order order = orderRepository.findOrderByIdAndMemberId(memberId, orderId);
        return new OrderResponse(order);
    }

    public List<OrderResponse> findOrdersByMemberId(final Long memberId) {
        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
