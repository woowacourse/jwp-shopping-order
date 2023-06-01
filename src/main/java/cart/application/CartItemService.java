package cart.application;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Fee;
import cart.domain.product.Product;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import cart.ui.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.controller.dto.request.CartItemRemoveRequest;
import cart.ui.controller.dto.request.CartItemRequest;
import cart.ui.controller.dto.response.CartItemPriceResponse;
import cart.ui.controller.dto.response.CartItemResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public CartItemPriceResponse getTotalPriceWithDeliveryFee(Member member, List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemRepository.findAllInIds(cartItemIds);
        for (CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        int totalPrice = calculateTotalPrice(cartItems);
        Fee deliveryFee = Fee.from(totalPrice);
        return new CartItemPriceResponse(totalPrice, deliveryFee.getValue());
    }

    private int calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.getProductById(cartItemRequest.getProductId());
        Optional<CartItem> searchCart = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId());

        if (searchCart.isPresent()) {
            CartItem cartItem = searchCart.get();
            CartItem updateCartItem = cartItem.changeQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.updateQuantity(updateCartItem);
            return updateCartItem.getId();
        }
        CartItem newCartItem = new CartItem(member, product);
        return cartItemRepository.save(newCartItem);
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }
        CartItem updateCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(updateCartItem);
    }

    @Transactional
    public void removeCartItems(Member member, CartItemRemoveRequest request) {
        List<CartItem> cartItems = cartItemRepository.findAllInIds(request.getCartItemIds());
        for (CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);
        cartItemRepository.deleteById(id);
    }
}
