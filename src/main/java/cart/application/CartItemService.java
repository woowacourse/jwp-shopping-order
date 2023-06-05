package cart.application;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
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
        CartItems cartItems = cartItemRepository.findByMemberId(member.getId());
        return CartItemResponse.listOf(cartItems);
    }

    public CartItemPriceResponse getTotalPriceWithDeliveryFee(Member member, List<Long> cartItemIds) {
        CartItems cartItems = cartItemRepository.findAllInIds(cartItemIds);
        cartItems.checkOwner(member);
        int totalPrice = cartItems.calculateTotalPrice();
        Fee deliveryFee = Fee.from(totalPrice);
        return new CartItemPriceResponse(totalPrice, deliveryFee.getValue());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId());
        Optional<CartItem> searchCart = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId());

        if (searchCart.isPresent()) {
            CartItem cartItem = searchCart.get();
            cartItem.changeQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.updateQuantity(cartItem);
            return cartItem.getId();
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
        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    @Transactional
    public void removeCartItems(Member member, CartItemRemoveRequest request) {
        CartItems cartItems = cartItemRepository.findAllInIds(request.getCartItemIds());
        cartItems.checkOwner(member);
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);
        cartItemRepository.deleteById(id);
    }
}
