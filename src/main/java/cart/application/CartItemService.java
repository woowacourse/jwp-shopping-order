package cart.application;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // TODO : dto 변환 로직 controller로 옮기기
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findCartItemByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.getProductById(cartItemRequest.getProductId());
        // TODO : CartItems에 추가하는 로직 생성 -> 만약 이미 product, memebr가 같으면 -> 수량을 추가할 수 있도록 변경
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
