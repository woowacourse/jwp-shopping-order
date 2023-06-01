package cart.application;

import static cart.exception.ErrorMessage.INVALID_CART_ITEM_OWNER;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.DeliveryFee;
import cart.domain.Member;
import cart.domain.Price;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CartItemsDeleteRequest;
import cart.dto.TotalPriceAndDeliveryFeeResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        CartItems cartItems = cartItemRepository.findByMemberId(member);

        return cartItems.getCartItems().stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId());

        return cartItemRepository.save(new CartItem(member, product));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        CartItem updatedCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(updatedCartItem);
    }

    @Transactional
    public void removeCartItem(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void removeCartItems(Member member, CartItemsDeleteRequest cartItemsDeleteRequest) {
        List<Long> cartItemIds = cartItemsDeleteRequest.getCartItemIds();
        List<CartItem> cartItems = cartItemIds.stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());

        boolean isNotOwner = cartItems.stream()
                .anyMatch(cartItem -> !cartItem.isSameOwner(member));

        if (isNotOwner) {
            throw new CartItemException(INVALID_CART_ITEM_OWNER);
        }

        cartItemRepository.deleteOrderedCartItem(cartItemIds);
    }

    public TotalPriceAndDeliveryFeeResponse getTotalPriceAndDeliveryFee(Member member, List<Long> cartItemIds) {
        CartItems cartItems = cartItemRepository.findByIds(member, cartItemIds);
        Price totalPrice = cartItems.getTotalPrice();
        DeliveryFee deliveryFee = DeliveryFee.calculate(totalPrice);

        return new TotalPriceAndDeliveryFeeResponse(totalPrice.getValue(), deliveryFee.getValue());
    }
}
