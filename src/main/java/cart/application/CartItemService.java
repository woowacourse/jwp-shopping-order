package cart.application;

import cart.db.repository.CartItemRepository;
import cart.db.repository.ProductRepository;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cart.exception.ErrorCode.INVALID_PRODUCT_ID;

@Service
public class CartItemService {
    
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    public void removeById(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void removeItems(final Member member, final List<Long> ids) {
        Long count = cartItemRepository.countByIdsAndMemberId(member.getId(), ids);
        if (count != ids.size()) {
            throw new BadRequestException(INVALID_PRODUCT_ID);
        }
        cartItemRepository.deleteByIds(member.getId(), ids);
    }
}
