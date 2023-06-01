package cart.service.cart;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.exception.MemberNotOwnerException;
import cart.repository.cart.CartRepository;
import cart.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final int EMPTY = 0;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(final CartRepository cartRepository, final ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(final Member member) {
        return cartRepository.findCartByMemberId(member.getId()).getCartItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Product product = productRepository.findProductById(cartItemRequest.getProductId());
        CartItem cartItem = cart.addItem(product);

        if (cartItem.isExistAlready()) {
            cartRepository.updateCartItemQuantity(cartItem);
            return cartItem.getId();
        }

        return cartRepository.insertNewCartItem(cart.getId(), cartItem);
    }

    @Transactional
    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        validateOwner(cart, cartItem);

        if (request.getQuantity() == EMPTY) {
            cart.removeItem(cartItem);
            cartRepository.removeCartItem(cartItem);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartRepository.updateCartItemQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);
        Cart cart = cartRepository.findCartByMemberId(member.getId());

        validateOwner(cart, cartItem);
        cart.removeItem(cartItem);

        cartRepository.deleteCartItemById(cartItem.getId());
    }

    private void validateOwner(final Cart cart, final CartItem cartItem) {
        if (!cart.hasCartItem(cartItem)) {
            throw new MemberNotOwnerException();
        }
    }
}
