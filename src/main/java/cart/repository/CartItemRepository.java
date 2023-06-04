package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.entity.CartItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CartItemRepository(CartItemDao cartItemDao,
                              MemberRepository memberRepository,
                              ProductRepository productRepository) {
        this.cartItemDao = cartItemDao;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(convertToEntity(cartItem));
    }

    public CartItem findById(Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id);
        return convertToDomain(memberRepository.findById(cartItemEntity.getMember_id()), cartItemEntity);
    }

    public List<CartItem> findByIds(Member member, List<Long> ids) {
        List<CartItemEntity> cartItemEntities = ids.stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList());

        return cartItemEntities.stream()
                .map(cartItemEntity -> convertToDomain(member, cartItemEntity))
                .collect(Collectors.toList());
    }

    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        Member member = memberRepository.findById(memberId);
        return cartItemEntities.stream()
                .map(cartItemEntity -> convertToDomain(member, cartItemEntity))
                .collect(Collectors.toList());
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(convertToEntity(cartItem));
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void deleteByIds(List<Long> cartItemIds) {
        cartItemIds.forEach(this::deleteById);
    }

    private CartItem convertToDomain(Member member, CartItemEntity cartItemEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                productRepository.findById(cartItemEntity.getProduct_id()),
                member
        );
    }

    private CartItemEntity convertToEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
    }
}
