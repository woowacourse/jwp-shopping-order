package cart.application;

import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;

    private Member member;
    private Product pizza;

    @BeforeEach
    void setup() {
        member = new Member(1L, "teo", "1234");
        pizza = new Product(1L, "피자", 20000, "https://a.com");
    }

    @Test
    void 멤버정보를_통해_장바구니_품목을_찾을_수_있다() {
        // given
        when(cartItemRepository.findByMemberId(member.getId())).thenReturn(List.of(
                new CartItem(1L, 1, pizza, member)
        ));

        // when
        List<CartItemResponse> carItemResponses = cartItemService.findByMember(member);

        // then
        assertThat(carItemResponses.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void 멤버정보를_통해_장바구니에_품목을_넣을_수_있다() {
        // given
        CartItemRequest request = new CartItemRequest(1L);
        Product product = new Product(1L, "teo", 10000, "https://");
        when(productRepository.findById(1L)).thenReturn(product);
        when(cartItemRepository.insert(any())).thenReturn(1L);

        // when
        Long cartItemId = cartItemService.add(member, request);

        // then
        assertThat(cartItemId).isEqualTo(1L);
    }
}
