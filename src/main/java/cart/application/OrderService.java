package cart.application;

import cart.db.repository.CouponRepository;
import cart.db.repository.OrderRepository;
import cart.db.repository.ProductRepository;
import cart.domain.Item;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.ItemRequest;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cart.exception.ErrorCode.INVALID_PRODUCT_ID;

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
    public void add(final Member member, final OrderRequest orderRequest) {
        Map<Long, Integer> orderRequestItems = orderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ItemRequest::getProductId, ItemRequest::getQuantity));

        List<Product> products = productRepository.findByIds(new ArrayList<>(orderRequestItems.keySet()));
        if (orderRequestItems.keySet().size() != products.size()) {
            throw new BadRequestException(INVALID_PRODUCT_ID);
        }

        List<Item> items = products.stream()
                .map(product -> new Item(product, orderRequestItems.get(product.getId())))
                .collect(Collectors.toUnmodifiableList());

        Coupon coupon = couponRepository.findById(orderRequest.getCouponId());
        Order order = new Order(member, items, coupon);
        orderRepository.save(order);
    }

    public OrderResponse findOrderById(final Member member, final Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.checkOwner(member);
        return new OrderResponse(order);
    }

    public List<OrderResponse> findOrdersByMemberId(final Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
