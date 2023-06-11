package cart.domain.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import cart.exception.MemberException;
import cart.exception.ProductException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public JdbcCartItemRepository(CartItemDao cartItemDao, ProductRepository productRepository, MemberRepository memberRepository) {
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void deleteById(long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }

    @Override
    public Optional<List<CartItem>> findCartItemsByIds(List<Long> cartIds) {
        try {
            List<CartItem> cartItems = cartIds.stream()
                    // TODO : map 내부에서 너무 많은 일이 일어나고 있다.
                    .map(cartId -> findCartItemById(cartId).orElseThrow(CartItemException.NotFound::new))
                    .collect(toList());
            return Optional.of(cartItems);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CartItem> findCartItemById(long cartId) {
        CartItemEntity cartItemEntity = cartItemDao.findById(cartId);
        Product product = productRepository.getProductById(cartItemEntity.getProductId())
                .orElseThrow(ProductException.NotFound::new);
        Member member = memberRepository.getMemberById(cartItemEntity.getMemberId())
                .orElseThrow();
        return Optional.of(new CartItem(cartId, cartItemEntity.getQuantity(), product, member));
    }

    @Override
    public Optional<List<CartItem>> findCartItemByMemberId(long memberId) {
        try {
            Member member = memberRepository.getMemberById(memberId).orElseThrow(MemberException.InvalidIdByNull::new);
            List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
            List<CartItem> cartItems = cartItemEntities.stream()
                    .map(cartItemEntity -> {
                        Product product = productRepository.getProductById(cartItemEntity.getProductId())
                                .orElseThrow(ProductException.NotFound::new);
                        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
                    }).collect(toList());
            return Optional.of(cartItems);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
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
