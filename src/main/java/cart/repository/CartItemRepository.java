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

    public CartItemRepository(CartItemDao cartItemDao, MemberRepository memberRepository,
                              ProductRepository productRepository) {
        this.cartItemDao = cartItemDao;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        Member member = memberRepository.findById(memberId);
        return cartItemEntities.stream()
                .map(cartItemEntity -> new CartItem(
                        cartItemEntity.getId(),
                        cartItemEntity.getQuantity(),
                        productRepository.findById(cartItemEntity.getProduct_id()),
                        member
                ))
                .collect(Collectors.toList());
    }
}
