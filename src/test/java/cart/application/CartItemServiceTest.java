package cart.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemAddRequest;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.BadRequestException;
import cart.exception.ForbiddenException;

class CartItemServiceTest {

    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private ProductDao productDao;

    private CartItemService cartItemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItemService = new CartItemService(productDao, cartItemDao);
    }

    @Test
    @DisplayName("멤버의 장바구니 물품을 반환한다.")
    void findByMember() {
        Member member = new Member(1L, "asdf@asdf.com", "asdf", "리오");
        CartItem cartItem1 = new CartItem(member, new Product(1L, "1", 1000, "http://www.aa.aa"));
        CartItem cartItem2 = new CartItem(member, new Product(2L, "2", 1000, "http://www.aa.aa"));
        when(cartItemDao.findByMemberId(member.getId())).thenReturn(List.of(cartItem1, cartItem2));

        List<CartItemResponse> cartItemResponses = cartItemService.findByMember(member);

        assertThat(cartItemResponses)
            .hasSize(2)
            .extracting(CartItemResponse::getProduct)
            .extracting("id")
            .containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("장바구니 물품을 추가한다.")
    void add() {
        Member member = new Member(1L, "asdf@asdf.com", "asdf", "리오");
        Product product1 = new Product(1L, "1", 1000, "http://www.aa.aa");
        Product product2 = new Product(2L, "2", 1000, "http://www.aa.aa");
        CartItem cartItem1 = new CartItem(member, product1);
        CartItemAddRequest cartItemAddRequest = new CartItemAddRequest(2L);
        when(cartItemDao.findByMemberId(member.getId())).thenReturn(List.of(cartItem1));
        when(productDao.findById(2L)).thenReturn(product2);
        when(cartItemDao.save(any(CartItem.class))).thenReturn(2L);

        Long newCartItemId = cartItemService.add(member, cartItemAddRequest);

        assertThat(newCartItemId).isEqualTo(2L);
        verify(cartItemDao).findByMemberId(member.getId());
        verify(productDao).findById(2L);
        verify(cartItemDao).save(any(CartItem.class));
    }

    @Test
    @DisplayName("장바구니 물품 수량을 업데이트한다.")
    void updateQuantity() {
        Member member = new Member(1L, "asdf@asdf.com", "asdf", "리오");
        CartItem cartItem = new CartItem(member, new Product(1L, "1", 1000, "http://www.aa.aa"));
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(3);
        when(cartItemDao.findById(1L)).thenReturn(cartItem);

        cartItemService.updateQuantity(member, 1L, request);

        assertThat(cartItem.getQuantity()).isEqualTo(3);
        verify(cartItemDao).findById(1L);
        verify(cartItemDao).updateQuantity(cartItem);
    }

    @Test
    @DisplayName("장바구니 물품을 제거한다.")
    void remove() {
        Member member = new Member(1L, "asdf@asdf.com", "asdf", "리오");
        CartItem cartItem = new CartItem(member, new Product(1L, "1", 1000, "http://www.aa.aa"));
        when(cartItemDao.findById(1L)).thenReturn(cartItem);

        cartItemService.remove(member, 1L);

        verify(cartItemDao).findById(1L);
        verify(cartItemDao).deleteById(1L);
    }

    @Test
    @DisplayName("장바구니 물품을 제거할 때 소유권이 일치하지 않으면 ForbiddenException을 던진다.")
    void remove_withMismatchedOwner() {
        Member member = new Member(1L, "asdf@asdf.com", "asdf", "리오");
        CartItem cartItem = new CartItem(new Member(2L, "other@member.com", "password", "Other"),
            new Product(1L, "1", 1000, "http://www.aa.aa"));
        when(cartItemDao.findById(1L)).thenReturn(cartItem);

        assertThrows(ForbiddenException.class, () -> cartItemService.remove(member, 1L));

        verify(cartItemDao).findById(1L);
        verify(cartItemDao, never()).deleteById(1L);
    }
}
