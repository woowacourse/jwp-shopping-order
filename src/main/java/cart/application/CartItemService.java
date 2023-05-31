package cart.application;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getCartItemsByMember(final Member member) {
        return cartItemRepository.findAllByMemberId(member.getId());
    }

    @Transactional
    public Long addCartItem(final Member member, final CartItemRequest request) {
        final Product product = productRepository.findById(request.getProductId());
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByMemberId(member.getId()));
        final Optional<CartItem> cartItemOptional = cartItems.findProduct(product);
        if (cartItemOptional.isEmpty()) {
            return cartItemRepository.save(new CartItem(member, product));
        }
        final CartItem cartItem = cartItemOptional.get();
        cartItem.checkOwner(member);
        final CartItem addedCartItem = cartItem.addQuantity();
        cartItemRepository.save(addedCartItem);

        return cartItem.getId();
    }

    @Transactional
    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }
        final CartItem updatedCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.save(updatedCartItem);
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.checkOwner(member);
        cartItemRepository.delete(cartItem);
    }
}
