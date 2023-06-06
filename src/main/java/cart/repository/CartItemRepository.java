package cart.repository;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.repository.dao.CartItemDao;
import cart.repository.entity.CartItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public CartItemRepository(CartItemDao cartItemDao, ProductRepository productRepository, MemberRepository memberRepository) {
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public void deleteById(long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }

    public List<CartItem> findCartItemsByIds(List<Long> cartIds) {
        return cartIds.stream()
                .map(this::findCartItemById)
                .collect(Collectors.toList());
    }

    public CartItem findCartItemById(long cartId) {
        CartItemEntity cartItemEntity = cartItemDao.findById(cartId);
        Product product = productRepository.getProductById(cartItemEntity.getProductId());
        Member member = memberRepository.getMemberById(cartItemEntity.getMemberId());
        return new CartItem(cartId, cartItemEntity.getQuantity(), product, member);
    }

    public List<CartItem> findCartItemByMemberId(long memberId) {
        Member member = memberRepository.getMemberById(memberId);
        List<CartItemEntity> cartItems = cartItemDao.findByMemberId(memberId);
        return cartItems.stream()
                .map(cartItemEntity -> {
                    Product product = productRepository.getProductById(cartItemEntity.getProductId());
                    return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
                }).collect(Collectors.toList());
    }

    public long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = toCartItemEntity(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    private CartItemEntity toCartItemEntity(CartItem cartItem) {
        return new CartItemEntity.Builder()
                .id(cartItem.getId())
                .memberId(cartItem.getMember().getId())
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }
}
