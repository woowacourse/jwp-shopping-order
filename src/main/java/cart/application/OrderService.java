package cart.application;

import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.OrderPageResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductOrderRequest;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductDao productDao;
    private final OrderRepository orderRepository;
    private final PointRepository pointRepository;
    private final OrderPage orderPage;
    private final PointAccumulationPolicy pointAccumulationPolicy;

    public OrderService(ProductDao productDao, OrderRepository orderRepository, PointRepository pointRepository,
                        OrderPage orderPage, PointAccumulationPolicy pointAccumulationPolicy) {
        this.productDao = productDao;
        this.orderRepository = orderRepository;
        this.pointRepository = pointRepository;
        this.orderPage = orderPage;
        this.pointAccumulationPolicy = pointAccumulationPolicy;
    }

    public OrderPageResponse findOrders(Member member, int pageNumber) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        List<OrderResponse> orderResponses = orders.stream()
                .skip(orderPage.calculateSkipNumber(pageNumber))
                .limit(orderPage.getLimit())
                .map(order -> new OrderResponse(order.getId(), order.getPayAmount(), order.getOrderAt(),
                        order.getFirstItemName(), order.getFirstItemImageUrl(), order.getItemSize()))
                .collect(Collectors.toList());
        return new OrderPageResponse(orderPage.calculateTotalPages(orders.size()), pageNumber, orderResponses);
    }

    public void saveOrder(Member member, OrderRequest orderRequest) {
        Points usablePoints = pointRepository.findUsablePointsByMemberId(member.getId());
        Points usePoints = usablePoints.getUsePoints(Point.from(orderRequest.getUsedPoint()));

        List<ProductOrderRequest> orderRequestProducts = orderRequest.getProducts();
        Map<Long, Product> productsById = getProducts(orderRequestProducts);

        List<OrderItem> orderItems = getOrderItems(orderRequestProducts, productsById);
        Order order = new Order(usePoints, orderItems);

        Long orderId = orderRepository.save(member.getId(), order);
        pointRepository.save(member.getId(), orderId, order.calculateSavedPoint(pointAccumulationPolicy));
    }

    private Map<Long, Product> getProducts(List<ProductOrderRequest> productOrderRequests) {
        List<Long> productIds = productOrderRequests.stream()
                .map(ProductOrderRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productDao.findAllByIds(productIds);

        Map<Long, Product> productsById = new HashMap<>();
        for (Product product : products) {
            productsById.put(product.getId(), product);
        }
        return productsById;
    }

    private List<OrderItem> getOrderItems(List<ProductOrderRequest> orderRequestProducts, Map<Long, Product> productsById) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (ProductOrderRequest orderRequestProduct : orderRequestProducts) {
            Long productId = orderRequestProduct.getProductId();
            Product product = productsById.get(productId);
            int quantity = orderRequestProduct.getQuantity();
            int totalCost = product.getPrice() * quantity;

            orderItems.add(new OrderItem(product, quantity, totalCost));
        }
        return orderItems;
    }
}
