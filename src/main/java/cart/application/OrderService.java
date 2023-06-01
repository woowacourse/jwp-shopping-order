package cart.application;

import cart.dao.CouponRepository;
import cart.dao.OrderRepository;
import cart.dao.ProductDao;
import cart.domain.Coupon;
import cart.domain.Item;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.ItemRequest;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
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
    private final ProductDao productDao;

    public OrderService(final OrderRepository orderRepository, final CouponRepository couponRepository, final ProductDao productDao) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.productDao = productDao;
    }

    @Transactional
    public void add(final Long memberId, final OrderRequest orderRequest) {
        Map<Long, Integer> itemsMap = orderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ItemRequest::getProductId, ItemRequest::getQuantity));

        List<Product> products = productDao.getProductsByIds(new ArrayList<>(itemsMap.keySet()));

        List<Item> items = products.stream()
                .map(product -> new Item(product, itemsMap.get(product.getId())))
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
