package cart.service;

import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemRepository cartItemRepository;

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        Product product = productDao.findById(cartItemRequest.getProductId());

        CartItem cartItem = new CartItem(member, product);

        return cartItemRepository.add(cartItem);
    }

    public List<CartItemResponse> findByMember(final Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());

        for (final CartItem cartItem : cartItems) {
            cartItem.validateOwner(member);
        }

        return cartItems.stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.validateOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        CartItem changedCartItem = cartItem.changeQuantity(new Quantity(request.getQuantity()));
        cartItemRepository.update(changedCartItem);
    }

    public void remove(final Member member, final Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.validateOwner(member);

        cartItemRepository.deleteById(id);
    }
}
