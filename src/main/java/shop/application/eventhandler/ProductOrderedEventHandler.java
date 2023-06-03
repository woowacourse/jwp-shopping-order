package shop.application.eventhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import shop.domain.event.ProductOrderedEvent;
import shop.domain.order.OrderItem;
import shop.domain.product.Product;
import shop.domain.repository.CartRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductOrderedEventHandler {
    private final CartRepository cartRepository;

    public ProductOrderedEventHandler(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @TransactionalEventListener(ProductOrderedEvent.class)
    public void removeCartItems(ProductOrderedEvent event) {
        List<Long> productIds = event.getOrderItems().stream()
                .map(OrderItem::getProduct)
                .map(Product::getId)
                .collect(Collectors.toList());

        cartRepository.deleteByMemberIdAndProductIds(event.getMemberId(), productIds);
    }
}
