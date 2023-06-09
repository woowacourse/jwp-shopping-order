package cart.application;

import cart.domain.cart.CartItem;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cart.fixture.CartItemFixture.장바구니1;
import static cart.fixture.CartItemFixture.장바구니2;
import static cart.fixture.MemberFixture.라잇;
import static cart.fixture.ProductFixture.지구;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    public void 회원으로_조회한다() {
        List<CartItem> cartItems = Arrays.asList(장바구니1, 장바구니2);
        when(cartItemRepository.findByMember(라잇)).thenReturn(cartItems);

        List<CartItemResponse> response = cartItemService.findByMember(라잇);

        assertThat(response).hasSize(2);
    }

    @Test
    public void 추가한다() {
        when(productRepository.findById(1L)).thenReturn(지구);
        when(cartItemRepository.findByMember(라잇)).thenReturn(Collections.EMPTY_LIST);
        CartItemRequest request = new CartItemRequest(1L);

        Long cartItemId = cartItemService.add(라잇, request);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    public void 수량을_수정한다() {
        when(cartItemRepository.findById(1L)).thenReturn(장바구니1);

        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(5);

        cartItemService.updateQuantity(라잇, 1L, request);

        verify(cartItemRepository, times(1)).update(any(CartItem.class));
    }

    @Test
    public void 삭제한다() {
        when(cartItemRepository.findById(1L)).thenReturn(장바구니1);

        cartItemService.remove(라잇, 1L);

        verify(cartItemRepository, times(1)).deleteById(1L);
    }
}
