package cart.repository.cart;

import cart.dao.cart.CartDao;
import cart.dao.member.MemberDao;
import cart.dao.product.ProductDao;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.entity.cart.CartEntity;
import cart.entity.cart.CartItemEntity;
import cart.entity.member.MemberEntity;
import cart.entity.product.ProductEntity;
import cart.exception.CartItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartRepository {

    private final MemberDao memberDao;
    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartRepository(final MemberDao memberDao, final CartDao cartDao, final ProductDao productDao) {
        this.memberDao = memberDao;
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    public Cart findCartByMemberId(final long memberId) {
        List<CartItemEntity> cartItemEntities = cartDao.findAllCartItemEntitiesByCartId(memberId);

        MemberEntity memberEntity = memberDao.getMemberById(memberId);
        Member member = new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());

        List<CartItem> cartItems = cartItemEntities.stream()
                .map(cartItemEntity -> {
                    int quantity = cartItemEntity.getQuantity();
                    Long productId = cartItemEntity.getProductId();
                    ProductEntity productEntity = productDao.getProductById(productId);
                    Product product = new Product(productId, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
                    return new CartItem(cartItemEntity.getId(), product, quantity);
                })
                .collect(Collectors.toList());

        CartEntity cartEntity = cartDao.findCartEntityByMemberId(memberId);
        return new Cart(cartEntity.getId(), member, new CartItems(cartItems));
    }

    public Long insertNewCartItem(final Long cartId, final CartItem cartItem) {
        return cartDao.saveCartItem(cartId, cartItem);
    }

    public CartItem findCartItemById(final Long cartItemId) {
        CartItemEntity cartItemEntity = cartDao.findCartItemEntityById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());

        Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
        return new CartItem(cartItemId, product, cartItemEntity.getQuantity());
    }

    public void removeCartItem(final CartItem cartItem) {
        cartDao.removeCartItem(cartItem.getId());
    }

    public void updateCartItemQuantity(final CartItem cartItem) {
        cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity());
    }

    public void deleteCartItemById(final Long cartItemId) {
        cartDao.removeCartItem(cartItemId);
    }
}
