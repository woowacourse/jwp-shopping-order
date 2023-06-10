package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.cart.CartItem;
import cart.exception.CartItemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixture.CartItemFixture.*;
import static cart.fixture.MemberFixture.라잇;
import static cart.fixture.MemberFixture.라잇_엔티티;
import static cart.fixture.ProductFixture.지구_엔티티;
import static cart.fixture.ProductFixture.화성_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private CartItemRepository cartItemRepository;

    @Test
    void 저장한다() {
        when(cartItemDao.save(장바구니1_엔티티)).thenReturn(장바구니1_엔티티.getId());

        Long savedCartItemId = cartItemRepository.save(장바구니1);

        assertThat(savedCartItemId).isEqualTo(장바구니1.getId());
    }

    @Test
    void 멤버로_장바구니를_조회한다() {
        List<CartItemEntity> cartItemEntities = List.of(장바구니1_엔티티, 장바구니2_엔티티);
        List<ProductEntity> productEntities = List.of(지구_엔티티, 화성_엔티티);
        when(productDao.findAll()).thenReturn(productEntities);
        when(cartItemDao.findByMemberId(라잇.getId())).thenReturn(cartItemEntities);

        List<CartItem> cartItems = cartItemRepository.findByMember(라잇);

        assertThat(cartItems).contains(장바구니1, 장바구니2);
    }
    @Test
    void 아이디로_조회한다() {
        when(cartItemDao.findById(장바구니1_엔티티.getId())).thenReturn(Optional.of(장바구니1_엔티티));
        when(productDao.findById(지구_엔티티.getId())).thenReturn(Optional.of(지구_엔티티));
        when(memberDao.findById(라잇_엔티티.getId())).thenReturn(Optional.of(라잇_엔티티));

        CartItem cartItem = cartItemRepository.findById(장바구니1_엔티티.getId());

        assertThat(cartItem).isEqualTo(장바구니1);
    }

    @Test
    void 아이디로_조회시_없으면_예외를_발생한다() {
        Long cartItemId = 1L;

        when(cartItemDao.findById(cartItemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartItemRepository.findById(cartItemId))
                .isInstanceOf(CartItemException.NoExist.class);
    }

    @Test
    void 아이디_목록으로_조회한다() {
        List<Long> cartItemIds = List.of(장바구니1.getId(), 장바구니2.getId());
        List<CartItemEntity> cartItemEntities = List.of(장바구니1_엔티티, 장바구니2_엔티티);
        when(cartItemDao.findByIds(cartItemIds)).thenReturn(cartItemEntities);
        when(productDao.findAll()).thenReturn(List.of(지구_엔티티, 화성_엔티티));
        when(memberDao.findAll()).thenReturn(List.of(라잇_엔티티));

        List<CartItem> cartItems = cartItemRepository.findByIds(cartItemIds);

        assertThat(cartItems).contains(장바구니1, 장바구니2);
    }

    @Test
    void 수정한다() {
        CartItem cartItem = new CartItem(1L, 장바구니1.getQuantity() + 1, 장바구니1.getProduct(), 장바구니1.getMember());
        CartItemEntity cartItemEntity = CartItemEntity.from(cartItem);

        cartItemRepository.update(cartItem);

        verify(cartItemDao).update(cartItemEntity);
    }

    @Test
    void 삭제한다() {
        Long cartItemId = 1L;

        cartItemRepository.deleteById(cartItemId);

        verify(cartItemDao).deleteById(cartItemId);
    }

    @Test
    void 여러항목을_삭제한다() {
        List<CartItem> cartItems = List.of(장바구니1, 장바구니2);
        List<Long> cartItemIds = List.of(장바구니1.getId(), 장바구니2.getId());

        cartItemRepository.deleteAll(cartItems);

        verify(cartItemDao).deleteAllByIds(cartItemIds);
    }
}

