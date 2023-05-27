package cart.service;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.cart.CartRepository;
import cart.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItemService(final CartRepository cartRepository, final ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());

        return cart.getCartItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

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

    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);

        if (request.getQuantity() == 0) {
            cart.removeItem(cartItem);
            cartRepository.removeCartItem(cartItem);
            return;
        }

        cart.changeQuantity(cartItem.getId(), request.getQuantity());
        cartItem.changeQuantity(request.getQuantity());
        cartRepository.updateCartItemQuantity(cartItem);
    }

    public void remove(final Member member, final Long cartItemId) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);

        cart.removeItem(cartItem);
        cartRepository.deleteCartItemById(cartItem.getId());
    }
}
