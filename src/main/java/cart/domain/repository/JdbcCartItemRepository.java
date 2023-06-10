package cart.domain.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final JdbcProductRepository productRepository;
    private final JdbcMemberRepository memberRepository;

    public JdbcCartItemRepository(CartItemDao cartItemDao, JdbcProductRepository productRepository, JdbcMemberRepository memberRepository) {
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void deleteById(long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }

    @Override
    public List<CartItem> findCartItemsByIds(List<Long> cartIds) {
        return cartIds.stream()
                .map(this::findCartItemById)
                .collect(Collectors.toList());
    }

    @Override
    public CartItem findCartItemById(long cartId) {
        CartItemEntity cartItemEntity = cartItemDao.findById(cartId)
                .orElseThrow(CartItemException.CartItemNotExists::new);
        Product product = productRepository.getProductById(cartItemEntity.getProductId());
        Member member = memberRepository.getMemberById(cartItemEntity.getMemberId());
        return new CartItem(cartId, cartItemEntity.getQuantity(), product, member);
    }

    @Override
    public List<CartItem> findCartItemByMemberId(long memberId) {
        Member member = memberRepository.getMemberById(memberId);
        List<CartItemEntity> cartItems = cartItemDao.findByMemberId(memberId);
        return cartItems.stream()
                .map(cartItemEntity -> {
                    Product product = productRepository.getProductById(cartItemEntity.getProductId());
                    return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
                }).collect(Collectors.toList());
    }

    @Override
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

    @Override
    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }
}
