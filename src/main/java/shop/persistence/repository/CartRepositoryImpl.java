package shop.persistence.repository;

import org.springframework.stereotype.Repository;
import shop.domain.cart.CartItem;
import shop.domain.member.EncryptedPassword;
import shop.domain.member.Member;
import shop.domain.member.MemberName;
import shop.domain.product.Product;
import shop.domain.repository.CartRepository;
import shop.exception.DatabaseException;
import shop.persistence.dao.CartDao;
import shop.persistence.entity.CartEntity;
import shop.persistence.entity.detail.CartItemDetail;

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
                cartItemDetail.getProductName(),
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
        CartItemDetail cartItemDetail = cartDao.findById(id);

        return toCartItem(cartItemDetail);
    }

    @Override
    public void deleteById(Long id) {
        cartDao.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        cartDao.deleteByIds(ids);
    }

    @Override
    public void deleteByMemberIdAndProductIds(Long memberId, List<Long> productIds) {
        cartDao.deleteByMemberIdAndProductIds(memberId, productIds);
    }

    @Override
    public void update(CartItem cartItem) {
        int countOfUpdatedCartItem = cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity());

        if (countOfUpdatedCartItem == 0) {
            throw new DatabaseException.IllegalDataException(
                    cartItem.getId() + "에 해당하는 장바구니 상품이 없습니다."
            );
        }
    }
}
