package cart.cartitem.application;

import cart.cartitem.domain.CartItem;
import cart.cartitem.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.dto.CartItemRequest;
import cart.cartitem.dto.CartItemResponse;
import cart.cartitem.repository.CartItemRepository;
import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    
    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }
    
    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        return cartItemRepository.findByMemberId(member.getId()).stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        final Product product = productRepository.getProductById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.removeById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        final CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.removeById(id);
    }
}
