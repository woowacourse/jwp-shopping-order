package cart.application;

import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.dto.cartitem.CartItemsResponse;
import cart.repository.CartItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        CartItems cartItems = cartItemRepository.findByMember(member);
        return cartItems.getItems().stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemRepository.add(member, cartItemRequest.getProductId());
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);
        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    public CartItemsResponse findCartItems(Member member, int size, int page) {
        CartItems cartItems = cartItemRepository.findCartItemsByPage(member, size, page);
        PageInfo pageInfo = cartItemRepository.findPageInfo(member, size, page);
        return CartItemsResponse.of(cartItems, pageInfo);
    }
}
