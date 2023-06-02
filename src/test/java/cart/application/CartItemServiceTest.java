package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemResponse;
import cart.dto.PagedCartItemsResponse;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.ProductDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

class CartItemServiceTest {

    private final ProductDao productDao = BDDMockito.mock(ProductDao.class);
    private final CartItemDao cartItemDao = BDDMockito.mock(CartItemDao.class);

    private CartItemService cartItemService;

    private final Member member = new Member(1L, "test@email.com", "password");
    private final List<CartItem> cartItems = List.of(
            new CartItem(1L, 1, new Product(1L, "상품1", 1_000, "www.example1.com"), member),
            new CartItem(2L, 2, new Product(2L, "상품2", 2_000, "www.example2.com"), member),
            new CartItem(3L, 3, new Product(3L, "상품3", 3_000, "www.example3.com"), member),
            new CartItem(4L, 4, new Product(4L, "상품4", 4_000, "www.example4.com"), member),
            new CartItem(5L, 5, new Product(5L, "상품5", 5_000, "www.example5.com"), member),
            new CartItem(6L, 6, new Product(6L, "상품6", 6_000, "www.example6.com"), member),
            new CartItem(7L, 7, new Product(7L, "상품7", 7_000, "www.example7.com"), member),
            new CartItem(8L, 8, new Product(8L, "상품8", 8_000, "www.example8.com"), member)
    );

    @BeforeEach
    void setUp() {
        cartItemService = new CartItemService(productDao, cartItemDao);
    }

    @DisplayName("페이지 정보에 맞게 정렬된 CartItem 목록을 반환한다")
    @Test
    void getPagedProducts() {
        // given
        willReturn(cartItems).given(cartItemDao).findByMemberId(member.getId());
        int unitSize = 3;
        int page = 2;

        // when
        final PagedCartItemsResponse pagedCartItems = cartItemService.getPagedCartItems(member, unitSize, page);

        // then
        assertThat(pagedCartItems.getCartItems()).map(CartItemResponse::getId)
                .containsExactly(5L, 4L, 3L);
        assertThat(pagedCartItems.getPagination().getCurrentPage()).isEqualTo(page);
        assertThat(pagedCartItems.getPagination().getPerPage()).isEqualTo(unitSize);
        assertThat(pagedCartItems.getPagination().getTotal()).isEqualTo(cartItems.size());
        assertThat(pagedCartItems.getPagination().getLastPage()).isEqualTo(3);
    }
}
