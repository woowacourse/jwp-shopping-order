package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.respository.cartitem.CartItemRepository;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.CartItemException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        final List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
            .map(CartItemResponse::of)
            .collect(Collectors.toList());
    }

    public CartItem add(Member member, CartItemRequest cartItemRequest) {
        final Product productById = productRepository.getProductById(cartItemRequest.getProductId())
            .orElseThrow(() -> new CartItemException.CartItemNotExisctException("장바구니 상품이 존재하지 않습니다."));
        return cartItemRepository.save(new CartItem(member, productById));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new CartItemException.CartItemNotExisctException("장바구니 상품이 존재하지 않습니다."));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        final CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new CartItemException.CartItemNotExisctException("장바구니 상품이 존재하지 않습니다."));

        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
