package cart.application;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.PagedCartItemsResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(final CartItemRepository cartItemRepository, final ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        final Optional<CartItem> findCartItem = cartItemRepository.findByMemberIdAndProductId(
                member.getId(),
                cartItemRequest.getProductId());
        
        if (findCartItem.isPresent()) {
            final CartItem updateCartItem = findCartItem.get();
            updateCartItem.changeQuantity(updateCartItem.getQuantity() + 1);
            cartItemRepository.updateQuantity(updateCartItem);
            return updateCartItem.getId();
        }

        final Product findProduct = productRepository.findById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(member, findProduct));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 장바구니 상품이 존재하지 않습니다"));

        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 장바구니 상품이 존재하지 않습니다"));

        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CartItemResponse findByCartItemId(final Member member, final Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 장바구니 상품이 존재하지 않습니다"));
        cartItem.checkOwner(member);

        return CartItemResponse.from(cartItem);
    }

    @Transactional(readOnly = true)
    public PagedCartItemsResponse getPagedCartItems(final Member member, final int unitSize, final int page) {
        final Pageable sortByIdDesc = PageRequest.of(page - 1, unitSize, Sort.by("id").descending());
        final Page<CartItem> pagedCartItems = cartItemRepository.getPagedCartItemsByMember(
                sortByIdDesc,
                member.getId()
        );

        return PagedCartItemsResponse.from(pagedCartItems);
    }
}
