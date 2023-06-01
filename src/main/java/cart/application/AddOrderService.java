package cart.application;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;
import cart.exception.IllegalOrderException;

@Service
public class AddOrderService {

    private final PointService pointService;
    private final CartItemService cartItemService;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    public AddOrderService(PointService pointService, CartItemService cartItemService, OrderDao orderDao, ProductDao productDao) {
        this.pointService = pointService;
        this.cartItemService = cartItemService;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Transactional
    public Long addOrder(Member member, PostOrderRequest request) {
        List<QuantityAndProduct> quantityAndProducts = mapToQuantityAndProducts(request);
        int totalPrice = quantityAndProducts.stream()
            .mapToInt(q -> q.getQuantity() * q.getProduct().getPrice())
            .sum();

        LocalDateTime now = LocalDateTime.now();
        int usePointAmount = request.getUsedPoint();
        int payAmount = totalPrice - usePointAmount;
        Long orderId = orderDao.insert(new Order(member, now, payAmount, OrderStatus.PENDING, quantityAndProducts));

        pointService.handlePointProcessInOrder(orderId, member.getId(), usePointAmount, payAmount, now);
        cartItemService.removeAllWithOutCheckingOwner(member.getId(), quantityAndProducts);
        return orderId;
    }

    private List<QuantityAndProduct> mapToQuantityAndProducts(PostOrderRequest request) {
        List<SingleKindProductRequest> requestProducts = request.getProducts();
        List<Long> productIds = requestProducts.stream()
            .map(SingleKindProductRequest::getProductId).distinct().collect(toList());
        List<Product> foundProducts = productDao.getProductsByIds(productIds);
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
}
