package cart.application;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.ProductRepository;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> findByMember(Member member) {
        return cartItemRepository.findCartItemByMemberId(member.getId());
    }

    public long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.getProductById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long cartItemId, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, long cartItemId) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        cartItem.checkOwner(member);
        cartItemRepository.deleteById(cartItemId);
    }
}
