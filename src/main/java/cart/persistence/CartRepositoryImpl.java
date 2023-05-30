package cart.persistence;

import cart.domain.cart.CartItem;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberName;
import cart.domain.product.Product;
import cart.domain.repository.CartRepository;
import cart.exception.PersistenceException;
import cart.persistence.dao.CartDao;
import cart.persistence.dto.CartItemDetail;
import cart.persistence.entity.CartEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private final CartDao cartDao;

    public CartRepositoryImpl(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    public Long save(CartItem cartItem) {
        CartEntity cartEntity = toEntity(cartItem);

        return cartDao.insert(cartEntity);
    }

    private CartEntity toEntity(CartItem cartItem) {
        return new CartEntity(cartItem.getMemberId(), cartItem.getProductId(), cartItem.getQuantity());
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        List<CartItemDetail> cartItemDetails = cartDao.findAllByMemberId(memberId);

        return cartItemDetails.stream()
                .map(this::toCartItem)
                .collect(Collectors.toList());
    }

    private CartItem toCartItem(CartItemDetail cartItemDetail) {
        Product product = createProduct(cartItemDetail);
        Member member = createMember(cartItemDetail);

        return new CartItem(
                cartItemDetail.getId(),
                cartItemDetail.getQuantity(),
                product,
                member
        );
    }

    private Product createProduct(CartItemDetail cartItemDetail) {
        return new Product(
                cartItemDetail.getProductId(),
                cartItemDetail.getMemberName(),
                cartItemDetail.getPrice(),
                cartItemDetail.getImageUrl()
        );
    }

    private Member createMember(CartItemDetail cartItemDetail) {
        return new Member(
                cartItemDetail.getMemberId(),
                new MemberName(cartItemDetail.getMemberName()),
                new EncryptedPassword(cartItemDetail.getPassword())
        );
    }

    @Override
    public CartItem findById(Long id) {
        CartItemDetail cartItemDetail = cartDao.findById(id)
                .orElseThrow(() -> new PersistenceException(id + "에 해당하는 장바구니 상품이 없습니다."));

        return toCartItem(cartItemDetail);
    }

    @Override
    public void deleteById(Long id) {
        cartDao.deleteById(id);
    }

    @Override
    public void update(CartItem cartItem) {
        int countOfUpdatedCartItem = cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity());

        if (countOfUpdatedCartItem == 0) {
            throw new PersistenceException(cartItem.getId() + "에 해당하는 장바구니 상품이 없습니다.");
        }
    }

    @Override
    public void deleteByMemberIdAndCartItemIds(Long memberId, List<Long> ids) {
        int countOfDeletedItem = cartDao.deleteByMemberIdAndCartItemIds(memberId, ids);

        if (countOfDeletedItem != ids.size()) {
            throw new PersistenceException("유효하지 않은 상품 ID가 존재합니다. " +
                    "요청 id : " + ids);
        }
    }
}
