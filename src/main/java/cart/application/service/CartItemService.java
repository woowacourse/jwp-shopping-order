package cart.application.service;

import static cart.exception.noexist.NoExistErrorType.CART_ITEM_NO_EXIST;
import static cart.exception.noexist.NoExistErrorType.PRODUCT_NO_EXIST;

import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.application.dto.cartitem.CartItemResponse;
import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.exception.noexist.NoExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository,
            final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMember(member);
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new NoExistException(PRODUCT_NO_EXIST));
        return cartItemRepository.create(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NoExistException(CART_ITEM_NO_EXIST));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NoExistException(CART_ITEM_NO_EXIST));
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
