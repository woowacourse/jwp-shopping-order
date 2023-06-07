package cart.application;

import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.PagedCartItemsResponse;
import cart.dto.PaginationInfoDto;
import cart.entity.ProductEntity;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.ProductDao;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        final Long productId = cartItemRequest.getProductId();
        final Optional<CartItem> findCartItem = getCartItem(member, productId);

        if (findCartItem.isPresent()) {
            final CartItem updateCartItem = findCartItem.get();
            updateCartItem.changeQuantity(updateCartItem.getQuantity() + 1);
            cartItemDao.updateQuantity(updateCartItem);
            return updateCartItem.getId();
        }

        final ProductEntity findProduct = productDao.getProductById(cartItemRequest.getProductId());
        return cartItemDao.save(new CartItem(member, Product.from(findProduct)));
    }

    private Optional<CartItem> getCartItem(final Member member, final Long productId) {
        final List<CartItem> memberCart = cartItemDao.findByMemberId(member.getId());
        return memberCart.stream()
                .filter(cartItem -> cartItem.equalsProductId(productId))
                .findFirst();
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CartItemResponse findByCartItemId(final Member member, final Long cartItemId) {
        final CartItem findCartItem = cartItemDao.findById(cartItemId);
        findCartItem.checkOwner(member);

        return CartItemResponse.from(findCartItem);
    }

    @Transactional(readOnly = true)
    public PagedCartItemsResponse getPagedCartItems(final Member member, final int unitSize, final int page) {
        final List<CartItem> allCartItems = cartItemDao.findByMemberId(member.getId());

        int startOrder = unitSize * (page - 1);
        final List<CartItem> pagedCartItems = allCartItems.stream()
                .sorted(Comparator.comparingLong(CartItem::getId).reversed())
                .skip(startOrder)
                .limit(unitSize)
                .collect(Collectors.toUnmodifiableList());

        final List<CartItemResponse> cartItemResponse = CartItemResponse.from(pagedCartItems);
        final int lastPage = (int) Math.ceil((double) allCartItems.size() / unitSize);
        final PaginationInfoDto paginationInfo = new PaginationInfoDto(allCartItems.size(), unitSize, page, lastPage);

        return new PagedCartItemsResponse(cartItemResponse, paginationInfo);
    }
}
