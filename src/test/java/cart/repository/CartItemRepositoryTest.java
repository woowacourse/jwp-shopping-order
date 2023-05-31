package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    private static final Member dummyMember = new Member(1L, "email1", "pw1", 1);
    private static final Product dummyProduct = new Product(1L, "name1", 1, "imageUrl1", 1);

    @Mock
    private MemberDao memberDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartItemDao cartItemDao;
    @InjectMocks
    private CartItemRepository repository;

    @Test
    void 새로운_객체를_추가한다() {
        // given
        CartItem cartItem = new CartItem(dummyMember, dummyProduct, 1);

        // when
        repository.add(cartItem);

        // then
        verify(cartItemDao).insert(any(CartItemEntity.class));
    }

    @Test
    void ID로_객체를_조회한다() {
        // given
        long id = 1L;

        CartItemEntity cartItemEntity = new CartItemEntity(1L, 1L, 1L, 1);
        when(cartItemDao.findById(id))
                .thenReturn(cartItemEntity);
        when(memberDao.findById(id))
                .thenReturn(dummyMember);
        when(productDao.findById(id))
                .thenReturn(dummyProduct);

        // when
        CartItem cartItem = repository.findById(id);

        // then
        CartItem expect = new CartItem(id, dummyMember, dummyProduct, 1);

        assertThat(cartItem).isEqualTo(expect);
    }
}
