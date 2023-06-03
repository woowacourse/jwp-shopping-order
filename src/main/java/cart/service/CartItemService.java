package cart.service;

import static java.util.stream.Collectors.toList;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.CartItemSaveRequest;
import cart.exception.cart.CartItemNotFoundException;
import cart.exception.cart.ProductNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(
            final CartItemRepository cartItemDao,
            final ProductRepository productDao
    ) {
        this.cartItemRepository = cartItemDao;
        this.productRepository = productDao;
    }

    public Long save(final Long memberId, final CartItemSaveRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        final CartItem cartItem = new CartItem(memberId, product);
        return cartItemRepository.save(cartItem).getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findAll(final Long memberId) {
        return cartItemRepository.findAllByMemberId(memberId).stream()
                .map(CartItemResponse::from)
                .collect(toList());
    }

    public void delete(final Long cartItemId, final Long memberId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        cartItem.checkOwner(memberId);
        cartItemRepository.deleteById(cartItemId);
    }

    public void updateQuantity(
            final Long memberId,
            final Long cartItemId,
            final CartItemQuantityUpdateRequest request
    ) {
        final CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        item.checkOwner(memberId);
        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        item.changeQuantity(request.getQuantity());
        cartItemRepository.save(item);
    }
}
