package cart.application;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.application.event.CartItemDeleteEvent;
import cart.application.event.PointEvent;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;
import cart.exception.IllegalOrderException;
import cart.repository.OrderRepository;

@Service
public class AddOrderService {

    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;
    private final ProductDao productDao;

    public AddOrderService(ApplicationEventPublisher publisher, OrderRepository orderRepository, ProductDao productDao) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Long addOrder(Member member, PostOrderRequest request) {
        List<QuantityAndProduct> quantityAndProducts = mapToQuantityAndProducts(request);
        int totalPrice = quantityAndProducts.stream()
            .mapToInt(q -> q.getQuantity() * q.getProduct().getPrice())
            .sum();

        LocalDateTime now = LocalDateTime.now();
        int payAmount = totalPrice - request.getUsedPoint();
        Long orderId = orderRepository.insert(new Order(member, now, payAmount, OrderStatus.PENDING, quantityAndProducts));

        handlePoint(member, request, now, payAmount, orderId);
        handleCartItemDeletion(member, quantityAndProducts);
        return orderId;
    }

    private List<QuantityAndProduct> mapToQuantityAndProducts(PostOrderRequest request) {
        List<SingleKindProductRequest> requestProducts = request.getProducts();
        List<Long> productIds = requestProducts.stream()
            .map(SingleKindProductRequest::getProductId).distinct().collect(toList());
        List<Product> foundProducts = productDao.findProductsByIds(productIds);
        validateProductsPresence(productIds, foundProducts);

        Map<Long, Product> productsById = foundProducts.stream()
            .collect(toMap(Product::getId, Function.identity()));
        return requestProducts.stream()
            .map(product -> new QuantityAndProduct(product.getQuantity(), productsById.get(product.getProductId())))
            .collect(toList());
    }

    private void validateProductsPresence(List<Long> productIds, List<Product> foundProducts) {
        if (productIds.size() != foundProducts.size()) {
            throw new IllegalOrderException("존재하지 않는 상품을 주문할 수 없습니다");
        }
    }

    private void handlePoint(Member member, PostOrderRequest request, LocalDateTime now, int payAmount, Long orderId) {
        publisher.publishEvent(new PointEvent(orderId, member.getId(), request.getUsedPoint(), payAmount, now));
    }

    private void handleCartItemDeletion(Member member, List<QuantityAndProduct> quantityAndProducts) {
        publisher.publishEvent(new CartItemDeleteEvent(member.getId(), quantityAndProducts));
    }
}
