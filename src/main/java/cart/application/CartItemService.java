package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMember(member);
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        List<CartItem> cartItems = cartItemRepository.findByMember(member);
        for (CartItem cartItem : cartItems) {
            if (Objects.equals(cartItem.getProduct().getId(), cartItemRequest.getProductId())) {
                throw new CartItemException.AlreadyExist("이미 장바구니에 존재하는 상품입니다.");
            }
        }
        return cartItemRepository.save(new CartItem(member, productRepository.findById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.update(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
