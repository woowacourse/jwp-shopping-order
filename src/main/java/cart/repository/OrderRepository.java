package cart.repository;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(OrderDao orderDao, OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderDto> orders = orderDao.findAllByMemberId(memberId);
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, List<OrderDto>> ordersByOrderId = orders.stream()
            .collect(groupingBy(OrderDto::getId));
        return ordersByOrderId.entrySet().stream()
            .map(entry -> toOrder(entry.getKey(), entry.getValue()))
            .collect(toList());
    }

    public Order findById(Long id) {
        List<OrderDto> orders = orderDao.findById(id);
        if (orders.isEmpty()) {
            return null;
        }
        return toOrder(id, orders);
    }

    private Order toOrder(Long id, List<OrderDto> orderDtos) {
        OrderDto orderDto1 = orderDtos.get(0);
        Member member = new Member(orderDto1.getMemberId(), orderDto1.getEmail(), orderDto1.getPassword());
        List<QuantityAndProduct> quantityAndProducts = orderDtos.stream()
            .map(orderDto -> {
                Product product = new Product(orderDto.getProductId(), orderDto.getName(), orderDto.getPrice(),
                    orderDto.getImageUrl());
                return new QuantityAndProduct(orderDto.getQuantity(), product);
            })
            .collect(toList());
        return new Order(id, member, orderDto1.getOrderAt(), orderDto1.getPayAmount(),
            OrderStatus.nameToEnum(orderDto1.getOrderStatus()), quantityAndProducts);
    }

    public Long insert(Order order) {
        OrderEntity orderEntity = new OrderEntity(order.getId(), order.getOrderAt(), order.getPayAmount(),
            order.getOrderStatus().getDisplayName());
        Long orderId = orderDao.insert(orderEntity);
        List<QuantityAndProduct> quantityAndProducts = order.getQuantityAndProducts();
        List<OrderProductEntity> orderProducts = quantityAndProducts.stream()
            .map(quantityAndProduct -> new OrderProductEntity(orderId, quantityAndProduct.getProduct().getId(),
                quantityAndProduct.getQuantity()))
            .collect(toList());
        orderProductDao.insertAll(orderProducts);
        return orderId;
    }
}
