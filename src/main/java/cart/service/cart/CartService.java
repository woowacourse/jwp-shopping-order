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
    public List<CartItemResponse> findAllCartItems(final Member member) {
        return cartRepository.findCartByMemberId(member.getId()).getCartItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Product product = productRepository.findProductById(cartItemRequest.getProductId());

        if (cartRepository.isExistAlreadyCartItem(cart, product)) {
            return updateCartItemQuantity(cartItemRequest, cart);
        }

        return makeNewCartItem(cartItemRequest, cart);
    }

    private Long updateCartItemQuantity(final CartItemRequest cartItemRequest, final Cart cart) {
        CartItem cartItem = cartRepository.findCartItem(cart, cartItemRequest.getProductId());

        cartRepository.addCartItemQuantity(cartItem);
        return cartItem.getId();
    }

    private Long makeNewCartItem(final CartItemRequest cartItemRequest, final Cart cart) {
        return cartRepository.insertNewCartItem(cart, cartItemRequest.getProductId());
    }

    @Transactional
    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        validateOwner(cart, cartItem);

        if (request.getQuantity() == EMPTY) {
            cartRepository.removeCartItem(cartItem);
            return;
        }

        cartRepository.updateCartItemQuantity(cartItem, request.getQuantity());
    }

    private void validateOwner(final Cart cart, final CartItem cartItem) {
        if (!cartRepository.hasCartItem(cart, cartItem)) {
            throw new MemberNotOwnerException();
        }
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        CartItem cartItem = cartRepository.findCartItemById(cartItemId);
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        validateOwner(cart, cartItem);

        cartRepository.deleteCartItemById(cartItem.getId());
    }

    @Transactional
    public void deleteAllCartItems(final Member member) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        cartRepository.deleteAllCartItems(cart);
    }
}
