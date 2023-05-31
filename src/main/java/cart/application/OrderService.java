package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.order.OrderProductRequest;
import cart.dto.order.OrderRequest;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponDao couponDao;

    public OrderService(final OrderRepository orderRepository, final CouponDao couponDao) {
        this.orderRepository = orderRepository;
        this.couponDao = couponDao;
    }

    public Long createOrder(List<OrderRequest> orderRequests, Long memberId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest orderRequest : orderRequests) {
            OrderProductRequest productRequest = orderRequest.getProductRequest();
            List<Coupon> coupons = orderRequest.getCouponId()
                    .stream()
                    .map(couponDao::findById)
                    .collect(Collectors.toList());
            orderItems.add(new OrderItem(new Product(
                    productRequest.getId(),
                    productRequest.getName(),
                    productRequest.getPrice(),
                    productRequest.getImageUrl()),
                    orderRequest.getQuantity(),
                    coupons
            ));
        }
        return orderRepository.create(orderItems, memberId);
    }

    public List<Order> findAll(Long memberId) {
        return orderRepository.findAllByMemberId(memberId);
    }
}
