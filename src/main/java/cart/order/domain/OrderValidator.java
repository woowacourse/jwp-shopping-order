package cart.order.domain;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;

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

    public void validate(List<CartItem> cartItems) {
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
        }
    }
}
