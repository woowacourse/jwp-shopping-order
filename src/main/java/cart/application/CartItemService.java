package cart.application;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.repository.CartRepository;
import cart.domain.repository.ProductRepository;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId());

        return cartRepository.save(new CartItem(member, product));
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartRepository.findAllByMemberId(member.getId());

        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartRepository.update(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }

    @Transactional
    public void removeById(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }

    @Transactional
    public void removeItems(final Member member, final List<Long> ids) {
        cartRepository.deleteByMemberIdAndCartItemIds(member.getId(), ids);
    }
}
