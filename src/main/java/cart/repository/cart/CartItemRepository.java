package cart.repository.cart;

import cart.dao.cart.CartItemDao;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.entity.CartItemEntity;
import cart.exception.cart.CartItemNotFoundException;
import cart.repository.member.MemberRepository;
import cart.repository.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartItemRepository {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartItemDao cartItemDao;

    public CartItemRepository(final ProductRepository productRepository, final MemberRepository memberRepository, final CartItemDao cartItemDao) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMember(final Member member) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            Product product = productRepository.findById(cartItemEntity.getProductId());
            cartItems.add(makeCartItem(cartItemEntity, product, member));
        }
        return cartItems;
    }

    public Long save(final CartItem cartItem) {
        return cartItemDao.save(makeCartItemEntity(cartItem));
    }

    public CartItem findById(final Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id).orElseThrow(CartItemNotFoundException::new);
        Product product = productRepository.findById(cartItemEntity.getProductId());
        Member member = memberRepository.findById(cartItemEntity.getMemberId());
        return makeCartItem(cartItemEntity, product, member);
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(final CartItem updateCartItem) {
        cartItemDao.updateQuantity(makeCartItemEntity(updateCartItem));
    }

    public List<CartItem> findByIds(final Member member, final List<Long> cartItemIds) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(cartItemIds);
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            Product product = productRepository.findById(cartItemEntity.getProductId());
            cartItems.add(makeCartItem(cartItemEntity, product, member));
        }
        return cartItems;
    }

    public void deleteByIds(final List<Long> cartItemIds) {
        cartItemDao.deleteByIds(cartItemIds);
    }

    private CartItem makeCartItem(final CartItemEntity cartItemEntity, final Product product, final Member member) {
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
    }

    private CartItemEntity makeCartItemEntity(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getQuantity(), cartItem.getMember().getId(), cartItem.getProduct().getId());
    }
}
