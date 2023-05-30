package cart.persistence.repository;

import static cart.persistence.mapper.CartItemMapper.convertCartItem;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItemRepository;
import cart.domain.cartitem.dto.CartItemSaveReq;
import cart.domain.cartitem.dto.CartItemWithId;
import cart.exception.DBException;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.entity.CartEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.CartItemMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public CartItemRepositoryImpl(final MemberDao memberDao, final CartItemDao cartItemDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItemWithId> findByMemberName(final String memberName) {
        return cartItemDao.findByMemberName(memberName).stream()
            .map(CartItemMapper::convertCartItemWithId)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Long save(final String memberName, final CartItemSaveReq cartItemSaveReq) {
        final MemberEntity memberEntity = getMemberEntity(memberName);
        final CartEntity cartEntity = new CartEntity(memberEntity.getId(), cartItemSaveReq.getCartItemId(),
            cartItemSaveReq.getCartItemQuantity());
        return cartItemDao.insert(cartEntity);
    }

    @Override
    public void deleteById(final Long cartItemId) {
        final int deletedCount = cartItemDao.deleteById(cartItemId);
        if (deletedCount != 1) {
            throw new DBException(ErrorCode.DB_DELETE_ERROR);
        }
    }

    @Override
    public void updateQuantity(final Long cartItemId, final int quantity) {
        final int updatedCount = cartItemDao.updateQuantity(cartItemId, quantity);
        if (updatedCount != 1) {
            throw new DBException(ErrorCode.DB_UPDATE_ERROR);
        }
    }

    @Override
    public CartItem findById(final Long cartItemId) {
        final CartItemDto cartItemDto = cartItemDao.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.CART_NOT_FOUND));
        return convertCartItem(cartItemDto);
    }

    @Override
    public Long countByIdsAndMemberId(final List<Long> cartItemIds, final String memberName) {
        final MemberEntity memberEntity = getMemberEntity(memberName);
        return cartItemDao.countByIdsAndMemberId(cartItemIds, memberEntity.getId());
    }

    @Override
    public void deleteByIdsAndMemberId(final List<Long> cartItemIds, final String memberName) {
        final MemberEntity memberEntity = getMemberEntity(memberName);
        cartItemDao.deleteByIdsAndMemberId(cartItemIds, memberEntity.getId());
    }

    private MemberEntity getMemberEntity(final String memberName) {
        return memberDao.findByName(memberName)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
