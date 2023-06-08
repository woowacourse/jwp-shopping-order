package cart.order.domain;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static cart.order.exception.OrderExceptionType.NO_AUTHORITY_ORDER_ITEM;

import cart.cartitem.domain.CartItem;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final ProductRepository productRepository;

    public OrderValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Long memberId, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId());
            if (!cartItem.getName().equals(product.getName())) {
                throw new OrderException(MISMATCH_PRODUCT);
            }
            if (cartItem.getProductPrice() != product.getPrice()) {
                throw new OrderException(MISMATCH_PRODUCT);
            }
            if (!cartItem.getImageUrl().equals(product.getImageUrl())) {
                throw new OrderException(MISMATCH_PRODUCT);
            }
            if (!cartItem.getMemberId().equals(memberId)) {
                throw new OrderException(NO_AUTHORITY_ORDER_ITEM);
            }
        }
    }
}
