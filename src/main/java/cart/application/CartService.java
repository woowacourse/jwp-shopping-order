package cart.application;

import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartRequest;
import cart.application.dto.cartitem.CartResponse;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItemWithId;
import cart.domain.cartitem.CartRepository;
import cart.domain.cartitem.dto.CartItemSaveReq;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.domain.product.dto.ProductWithId;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import cart.exception.ForbiddenException;
import cart.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private static final int EMPTY_CART_ITEM_QUANTITY = 0;
    private static final int INIT_CART_ITEM_QUANTITY = 1;

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CartService(final CartRepository cartRepository, final MemberRepository memberRepository,
                       final ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<CartResponse> findByMember(final String memberName) {
        final Member member = memberRepository.findByName(memberName);
        final Cart cart = cartRepository.findByMemberName(member.name());

        return cart.getCartItems().stream()
            .map(this::convertCartItemResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public long addCart(final String memberName, final CartRequest cartRequest) {
        final Long productId = cartRequest.getProductId();
        validateProduct(productId);
        validateAlreadyAdd(memberName, productId);
        final CartItemSaveReq cartItemSaveReq = new CartItemSaveReq(productId, INIT_CART_ITEM_QUANTITY);
        return cartRepository.save(memberName, cartItemSaveReq);
    }

    @Transactional
    public void updateQuantity(final String memberName, final Long cartItemId,
                               final CartItemQuantityUpdateRequest updateRequest) {
        final Cart cart = cartRepository.findById(cartItemId);
        cart.checkOwner(memberName);

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
        cart.checkOwner(memberName);
        cartRepository.deleteById(cartItemId);
    }

    @Transactional
    public void removeItems(final String memberName, final List<Long> cartItemIds) {
        final Long count = cartRepository.countByCartItemIdsAndMemberId(cartItemIds, memberName);
        if (count != cartItemIds.size()) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        cartRepository.deleteByCartItemIdsAndMemberId(cartItemIds, memberName);
    }

    private void validateProduct(final Long productId) {
        if (!productRepository.existById(productId)) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    private void validateAlreadyAdd(final String memberName, final Long productId) {
        if (cartRepository.existByMemberNameAndProductId(memberName, productId)) {
            throw new BadRequestException(ErrorCode.CART_ALREADY_ADD);
        }
    }

    private CartResponse convertCartItemResponse(final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        final Product product = productWithId.getProduct();
        return new CartResponse(cartItemWithId.getCartId(), cartItemWithId.getQuantity(),
            new ProductResponse(productWithId.getProductId(), product.getName(), product.getPrice(),
                product.getImageUrl()));
    }
}
