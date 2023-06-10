package cart.repository;

import static cart.fixture.CartItemFixture.CART_ITEM_1;
import static cart.fixture.CartItemFixture.CART_ITEM_2;
import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.PageInfoFixture.PAGE_INFO_1;
import static cart.fixture.ProductFixture.PRODUCT_1;
import static cart.fixture.ProductFixture.PRODUCT_2;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(CartItemRepositoryImpl.class)
class CartItemRepositoryImplTest {

    public static final CartItemEntity CART_ITEM_ENTITY_1 = new CartItemEntity(CART_ITEM_1.getId(),
            CART_ITEM_1.getMember().getId(), CART_ITEM_1.getProduct().getId(), CART_ITEM_1.getQuantity());
    public static final CartItemEntity CART_ITEM_ENTITY_2 = new CartItemEntity(CART_ITEM_2.getId(),
            CART_ITEM_2.getMember().getId(), CART_ITEM_2.getProduct().getId(), CART_ITEM_2.getQuantity());
    public static final ProductEntity PRODUCT_ENTITY_1 = new ProductEntity(PRODUCT_1.getId(), PRODUCT_1.getName(),
            PRODUCT_1.getPrice(), PRODUCT_1.getImageUrl());
    public static final ProductEntity PRODUCT_ENTITY_2 = new ProductEntity(PRODUCT_2.getId(), PRODUCT_1.getName(),
            PRODUCT_2.getPrice(), PRODUCT_2.getImageUrl());

    @Autowired
    CartItemRepository cartItemRepository;

    @MockBean
    ProductDao productDao;

    @MockBean
    CartItemDao cartItemDao;

    @MockBean
    MemberDao memberDao;

    @Test
    @DisplayName("사용자로 장바구니 상품들을 조회한다.")
    void findByMember() {
        // given
        willReturn(List.of(CART_ITEM_ENTITY_1, CART_ITEM_ENTITY_2)).given(cartItemDao).findByMemberId(anyLong());
        willReturn(PRODUCT_ENTITY_1).given(productDao).getProductById(CART_ITEM_1.getProduct().getId());
        willReturn(PRODUCT_ENTITY_2).given(productDao).getProductById(CART_ITEM_2.getProduct().getId());

        // when
        CartItems cartItems = cartItemRepository.findByMember(MEMBER_1);

        // then
        assertThat(cartItems.getItems())
                .extracting(CartItem::getId)
                .contains(CART_ITEM_ENTITY_1.getId(), CART_ITEM_ENTITY_2.getId());
    }

    @Test
    @DisplayName("장바구니 상품을 추가한다.")
    void add() {
        // given
        willReturn(false).given(cartItemDao).existsByProductIdAndMemberId(anyLong(), anyLong());
        willReturn(PRODUCT_ENTITY_1).given(productDao).getProductById(CART_ITEM_1.getProduct().getId());
        willReturn(CART_ITEM_1.getId()).given(cartItemDao).save(any());

        // when
        Long id = cartItemRepository.add(MEMBER_1, PRODUCT_1.getId());

        // then
        assertThat(id).isEqualTo(CART_ITEM_1.getId());
    }

    @Test
    @DisplayName("장바구니 상품을 추가할 때, 이미 존재하는 상품이면 수량을 변경한다.")
    void add_update() {
        // given
        willReturn(true).given(cartItemDao).existsByProductIdAndMemberId(anyLong(), anyLong());
        willReturn(List.of(CART_ITEM_ENTITY_1)).given(cartItemDao).findByMemberId(anyLong());
        willReturn(PRODUCT_ENTITY_1).given(productDao).getProductById(anyLong());
        willDoNothing().given(cartItemDao).updateQuantity(any());

        // when
        Long id = cartItemRepository.add(MEMBER_1, PRODUCT_1.getId());

        // then
        assertThat(id).isEqualTo(CART_ITEM_1.getId());
    }

    @Test
    @DisplayName("장바구니 상품의 수량을 변경한다.")
    void updateQuantity() {
        // given
        willDoNothing().given(cartItemDao).updateQuantity(any());

        // when, then
        assertDoesNotThrow(
                () -> cartItemRepository.updateQuantity(CART_ITEM_1)
        );
    }

    @Test
    @DisplayName("장바구니 상품의 수량을 0으로 변경하면 장바구니에서 상품이 삭제된다.")
    void updateQuantity_zero() {
        // given
        willDoNothing().given(cartItemDao).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(
                () -> cartItemRepository.updateQuantity(new CartItem(1L, 0, PRODUCT_1, MEMBER_1))
        );
    }

    @Test
    @DisplayName("ID로 장바구니 상품을 조회한다.")
    void findById() {
        // given
        willReturn(CART_ITEM_ENTITY_1).given(cartItemDao).findById(anyLong());
        willReturn(MEMBER_1).given(memberDao).getMemberById(CART_ITEM_ENTITY_1.getMemberId());
        willReturn(PRODUCT_ENTITY_1).given(productDao).getProductById(PRODUCT_1.getId());

        // when
        CartItem cartItem = cartItemRepository.findById(CART_ITEM_1.getId());

        // then
        assertThat(cartItem)
                .usingRecursiveComparison()
                .isEqualTo(CART_ITEM_1);
    }

    @Test
    @DisplayName("ID로 장바구니 상품을 삭제한다.")
    void deleteById() {
        // given
        willDoNothing().given(cartItemDao).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(
                () -> cartItemRepository.deleteById(CART_ITEM_1.getId())
        );
    }

    @Test
    @DisplayName("특정 페이지의 장바구니 상품들을 조회한다.")
    void findCartItemsByPage() {
        // given
        willReturn(List.of(CART_ITEM_ENTITY_1, CART_ITEM_ENTITY_2)).given(cartItemDao)
                .findCartItemsByPage(CART_ITEM_1.getMember().getId(), 2, 1);
        willReturn(PRODUCT_ENTITY_1).given(productDao).getProductById(CART_ITEM_1.getProduct().getId());
        willReturn(PRODUCT_ENTITY_2).given(productDao).getProductById(CART_ITEM_2.getProduct().getId());

        // when
        CartItems cartItems = cartItemRepository.findCartItemsByPage(MEMBER_1, 2, 1);

        // then
        assertThat(cartItems.getItems())
                .hasSize(2)
                .extracting(CartItem::getId)
                .contains(CART_ITEM_ENTITY_1.getId(), CART_ITEM_ENTITY_2.getId());
    }

    @Test
    @DisplayName("특정 페이지의 장바구니 상품들을 조회할 때, 페이지 정보를 조회한다.")
    void findPageInfo() {
        // given
        willReturn(PAGE_INFO_1).given(cartItemDao).findPageInfo(MEMBER_1.getId(), 1, 1);

        // when
        PageInfo pageInfo = cartItemRepository.findPageInfo(MEMBER_1, 1, 1);

        // then
        assertThat(pageInfo)
                .usingRecursiveComparison()
                .isEqualTo(PAGE_INFO_1);
    }
}
