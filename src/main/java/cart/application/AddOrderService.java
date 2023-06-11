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
import cart.application.event.PointAdditionEvent;
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
        List<SingleKindProductRequest> singleKindProducts = request.getProducts();
        List<Long> productIds = collectProductIds(singleKindProducts);
        Map<Long, Product> productsById = findProducts(productIds);
        validateProductsPresence(productIds, productsById);

        List<QuantityAndProduct> quantityAndProducts = toQuantityAndProducts(singleKindProducts, productsById);
        int totalPrice = getTotalPrice(quantityAndProducts);
        LocalDateTime now = LocalDateTime.now();
        int payAmount = totalPrice - request.getUsedPoint();
        Long orderId = insertOrder(member, now, payAmount, quantityAndProducts);

        publisher.publishEvent(new PointAdditionEvent(orderId, member.getId(), request.getUsedPoint(), payAmount, now));
        publisher.publishEvent(new CartItemDeleteEvent(member.getId(), quantityAndProducts));
        return orderId;
    }

    private List<Long> collectProductIds(List<SingleKindProductRequest> requestProducts) {
        return requestProducts.stream()
            .map(SingleKindProductRequest::getProductId)
            .distinct()
            .collect(toList());
    }

    private Map<Long, Product> findProducts(List<Long> productIds) {
        List<Product> persistProducts = productDao.findProductsByIds(productIds);
        return persistProducts.stream()
            .collect(toMap(Product::getId, Function.identity()));
    }

    private void validateProductsPresence(List<Long> productIds, Map<Long, Product> productsById) {
        if (productIds.size() != productsById.keySet().size()) {
            throw new IllegalOrderException("존재하지 않는 상품을 주문할 수 없습니다");
        }
    }

    private List<QuantityAndProduct> toQuantityAndProducts(List<SingleKindProductRequest> singleKindProducts,
        Map<Long, Product> productsById) {
        return singleKindProducts.stream()
            .map(singleKindProduct -> new QuantityAndProduct(singleKindProduct.getQuantity(),
                productsById.get(singleKindProduct.getProductId())))
            .collect(toList());
    }

    private Long insertOrder(
        Member member, LocalDateTime now, int payAmount, List<QuantityAndProduct> quantityAndProducts
    ) {
        Order order = new Order(member, now, payAmount, OrderStatus.PENDING, quantityAndProducts);
        return orderRepository.insert(order);
    }

    private int getTotalPrice(List<QuantityAndProduct> quantityAndProducts) {
        return quantityAndProducts.stream()
            .mapToInt(QuantityAndProduct::getTotalPrice)
            .sum();
    }
}
