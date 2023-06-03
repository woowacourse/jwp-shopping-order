package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.CheckoutResponse;
import cart.exception.CartItemException;
import cart.exception.CartItemException.NotFound;
import cart.exception.ProductException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductException.NotFound(cartItemRequest.getProductId()));

        CartItem cartItem = new CartItem(member, product);

        return cartItemRepository.save(member, cartItem);
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMember(member);

        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Member member, Long cartItemId, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(NotFound::new);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);

            return ;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(NotFound::new);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    public CheckoutResponse checkout(Member member, List<Long> checkedCartItemIds) {
        List<CartItem> memberCartItems = cartItemRepository.findByMember(member);
        List<CartItem> checkedCartItems = calculateCheckedCartItems(checkedCartItemIds, memberCartItems);

        return CheckoutResponse.of(checkedCartItems, member);
    }

    private List<CartItem> calculateCheckedCartItems(List<Long> checkedCartItemIds, List<CartItem> memberCartItems) {
        List<CartItem> checkedCartItems = memberCartItems.stream()
                .filter(memberCartItem -> checkedCartItemIds.contains(memberCartItem.getId()))
                .collect(Collectors.toList());

        validateCheckedCartItems(checkedCartItemIds, checkedCartItems);

        return checkedCartItems;
    }

    private void validateCheckedCartItems(List<Long> checkedCartItemIds, List<CartItem> checkedCartItems) {
        if (checkedCartItemIds.size() != checkedCartItems.size()) {
            throw new CartItemException.NotFound();
        }
    }
}
