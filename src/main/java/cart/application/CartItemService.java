package cart.application;

import cart.domain.cartitem.CartItemRepository;
import cart.domain.product.ProductRepository;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findAllCartItemsByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findProductById(cartItemRequest.getProductId())
                .orElseThrow(() -> new CartException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItemRepository.findCartItemByMemberIdAndProductId(member.getId(), cartItemRequest.getProductId())
                .ifPresent(cartItem -> {
                    throw new CartException(ErrorCode.CART_ITEM_DUPLICATED);
                });
        return cartItemRepository.save(new CartItem(member, product));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findCartItemById(id)
                .orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.checkOwner(member);
        if (cartItem.haveNoProduct()) {
            throw new CartException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findCartItemById(id)
                .orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (cartItem.haveNoProduct()) {
            throw new CartException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        cartItem.checkOwner(member);
        cartItemRepository.deleteById(id);
    }
}
