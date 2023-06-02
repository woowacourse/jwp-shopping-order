package cart.application;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import cart.ui.dto.cartitem.CartItemIdsRequest;
import cart.ui.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.ui.dto.cartitem.CartItemRequest;
import cart.ui.dto.order.PaymentInfoResponse;
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
        final CartItem updatedCartItem = cartItem.updateQuantity(request.getQuantity());
        cartItemRepository.save(updatedCartItem);
    }

    @Transactional
    public void removeCartItem(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.checkOwner(member);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void removeCartItems(final Member member, final CartItemIdsRequest request) {
        final List<CartItem> cartItems = cartItemRepository.findAllByIds(request.getCartItemIds());
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        cartItemRepository.deleteAll(cartItems);
    }

    public PaymentInfoResponse getPaymentInfo(final Member member, final List<Long> cartItemIds) {
        final CartItems cartItems = new CartItems(cartItemRepository.findAllByIds(cartItemIds));
        cartItems.checkOwner(member);

        return new PaymentInfoResponse(cartItems.getTotalPrice(), cartItems.getDeliveryFee());
    }
}
