package cart.application;

import static java.util.stream.Collectors.joining;

import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartItemRequest;
import cart.application.dto.cartitem.CartResponse;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItemWithId;
import cart.domain.cartitem.CartRepository;
import cart.domain.cartitem.dto.CartItemSaveReq;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;
import cart.exception.ForbiddenException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private static final int EMPTY_CART_ITEM_QUANTITY = 0;
    private static final int INIT_CART_ITEM_QUANTITY = 1;

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    public CartService(final MemberRepository memberRepository, final CartRepository cartRepository) {
        this.memberRepository = memberRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartResponse> findByMember(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        final Cart cart = cartRepository.findByMemberName(member.name());

        return cart.getCartItems().stream()
            .map(this::convertCartItemResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public long addCart(final String memberName, final CartItemRequest cartItemRequest) {
        final CartItemSaveReq cartItemSaveReq = new CartItemSaveReq(cartItemRequest.getProductId(),
            INIT_CART_ITEM_QUANTITY);
        return cartRepository.save(memberName, cartItemSaveReq);
    }

    @Transactional
    public void updateQuantity(final String memberName, final Long cartItemId,
                               final CartItemQuantityUpdateRequest updateRequest) {
        final Cart cart = cartRepository.findById(cartItemId);
        cart.checkOwner(cartItemId, memberName);

        final int quantity = updateRequest.getQuantity();
        if (quantity == EMPTY_CART_ITEM_QUANTITY) {
            cartRepository.deleteById(cartItemId);
            return;
        }
        final CartItemWithId cartItem = cart.getCartItems().get(0);
        cartItem.changeQuantity(quantity);
        cartRepository.updateQuantity(cartItemId, quantity);
    }

    @Transactional
    public void remove(final String memberName, Long cartItemId) {
        final Cart cart = cartRepository.findById(cartItemId);
        cart.checkOwner(cartItemId, memberName);
        cartRepository.deleteById(cartItemId);
    }

    @Transactional
    public void removeItems(final String memberName, final List<Long> cartItemIds) {
        final Long count = cartRepository.countByCartItemIdsAndMemberId(cartItemIds, memberName);
        if (count != cartItemIds.size()) {
            final String cartItemId = cartItemIds.stream().map(String::valueOf).collect(joining(", "));
            throw new ForbiddenException(cartItemId, memberName);
        }
        cartRepository.deleteByCartItemIdsAndMemberId(cartItemIds, memberName);
    }

    private CartResponse convertCartItemResponse(final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        final Product product = productWithId.getProduct();
        return new CartResponse(cartItemWithId.getId(), cartItemWithId.getQuantity(),
            new ProductResponse(productWithId.getProductId(), product.getName(), product.getPrice(),
                product.getImageUrl()));
    }
}
