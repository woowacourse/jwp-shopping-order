package cart.application;

import static java.util.stream.Collectors.joining;

import cart.application.dto.CartItemQuantityUpdateRequest;
import cart.application.dto.CartItemRequest;
import cart.application.dto.CartItemResponse;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItemRepository;
import cart.domain.cartitem.dto.CartItemSaveReq;
import cart.domain.cartitem.dto.CartItemWithId;
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
public class CartItemService {

    private static final int EMPTY_CART_ITEM_QUANTITY = 0;
    private static final int INIT_CART_ITEM_QUANTITY = 1;

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final MemberRepository memberRepository, final CartItemRepository cartItemRepository) {
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        final List<CartItemWithId> cartItems = cartItemRepository.findByMemberName(member.name());
        return cartItems.stream()
            .map(this::convertCartItemResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public long addCart(final String memberName, final CartItemRequest cartItemRequest) {
        final CartItemSaveReq cartItemSaveReq = new CartItemSaveReq(cartItemRequest.getProductId(),
            INIT_CART_ITEM_QUANTITY);
        return cartItemRepository.save(memberName, cartItemSaveReq);
    }

    @Transactional
    public void updateQuantity(final String memberName, final Long cartItemId,
                               final CartItemQuantityUpdateRequest updateRequest) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.checkOwner(cartItemId, memberName);

        final int quantity = updateRequest.getQuantity();
        if (quantity == EMPTY_CART_ITEM_QUANTITY) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }
        cartItem.changeQuantity(quantity);
        cartItemRepository.updateQuantity(cartItemId, quantity);
    }

    @Transactional
    public void remove(final String memberName, Long cartItemId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.checkOwner(cartItemId, memberName);
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void removeItems(final String memberName, final List<Long> cartItemIds) {
        final Long count = cartItemRepository.countByIdsAndMemberId(cartItemIds, memberName);
        if (count != cartItemIds.size()) {
            final String cartItemId = cartItemIds.stream().map(String::valueOf).collect(joining(", "));
            throw new ForbiddenException(cartItemId, memberName);
        }
        cartItemRepository.deleteByIdsAndMemberId(cartItemIds, memberName);
    }

    private CartItemResponse convertCartItemResponse(final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        final Product product = productWithId.getProduct();
        return new CartItemResponse(cartItemWithId.getId(), cartItemWithId.getQuantity(),
            new ProductResponse(productWithId.getId(), product.getName(), product.getPrice(),
                product.getImageUrl()));
    }
}
