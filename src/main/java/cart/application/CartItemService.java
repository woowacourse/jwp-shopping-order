package cart.application;

import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Quantity;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(
        ProductRepository productRepository,
        CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        Cart cart = cartItemRepository.findByMember(member);
        return cart.getItems()
            .stream()
            .map(CartItemResponse::of)
            .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemRepository.save(
            new CartItem(member, productRepository.findById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }

        cartItem.changeQuantity(Quantity.from(request.getQuantity()));
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.delete(cartItem);
    }

    public void removeOrderedItems(Cart cart) {
        List<Long> cartItemIds = cart.getCartItemIds();
        cartItemRepository.removeAllByIds(cartItemIds);
    }

    @Transactional(readOnly = true)
    public Cart findAllByIds(List<Long> ids, Member member) {
        Cart cart = cartItemRepository.findAllByIds(ids);
        cart.validateOwner(member);
        return cart;
    }
}
