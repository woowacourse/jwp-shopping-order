package cart.application;

import cart.application.request.CreateCartItemRequest;
import cart.application.request.UpdateCartItemQuantityRequest;
import cart.application.response.CartItemResponse;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.vo.Quantity;
import cart.entity.CartItemEntity;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Long addCartItem(Member member, CreateCartItemRequest request) {
        Product findProduct = productRepository.findByProductId(request.getProductId());
        CartItemEntity cartItemEntity = new CartItemEntity(findProduct.getId(), member.getId(), 1);

        return cartItemRepository.saveCartItem(cartItemEntity);
    }

    public List<CartItemResponse> findByMember(Member member) {
        CartItems cartItems = cartItemRepository.findByMemberId(member.getId());

        return cartItems.getCartItems()
                .stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Member member, Long cartItemId, UpdateCartItemQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findByCartItemId(cartItemId);

        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteByCartItemId(cartItemId);
            return;
        }

        cartItem.changeQuantity(Quantity.from(request.getQuantity()));
        cartItemRepository.updateQuantity(cartItem.getId(), cartItem.getQuantity().getValue());
    }

    @Transactional
    public void remove(Member member, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findByCartItemId(cartItemId);
        cartItem.checkOwner(member);

        cartItemRepository.deleteByCartItemId(cartItemId);
    }
}
