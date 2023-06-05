package cart.integration;

import static java.util.Base64.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.dto.CartItemQuantityUpdateRequest;
import cart.application.dto.CartItemRequest;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;

    private Long productId1;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();
        productId1 = productDao.createProduct(new Product("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = productDao.createProduct(new Product("피자", 15_000, "http://example.com/pizza.jpg"));

        memberDao.addMember(new Member(1L, "a@a.com", "1234"));
        memberDao.addMember(new Member(2L, "b@b.com", "1234"));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() throws Exception {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        // when
        ResultActions response = requestAddCartItem(member, cartItemRequest);

        // then
        response.andExpect(status().isCreated());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() throws Exception {
        // given
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(productId1);

        // when
        ResultActions response = requestAddCartItem(illegalMember, cartItemRequest);

        // then
        response.andExpect(status().isUnauthorized());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() throws Exception {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId1);
        Long cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        // when
        ResultActions response = requestGetCartItems(member);

        // then
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(cartItemId1))
            .andExpect(jsonPath("$[1].id").value(cartItemId2));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() throws Exception {
        // given
        Long cartItemId = requestAddCartItemAndGetId(member, productId1);

        // when
        requestUpdateCartItemQuantity(member, cartItemId, 10);

        // then
        ResultActions cartItems = requestGetCartItems(member);
        cartItems.andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(cartItemId))
            .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() throws Exception {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId1);
        Long cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        // when
        requestUpdateCartItemQuantity(member, cartItemId1, 0);

        // then
        String response = requestGetCartItems(member).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        assertThat(response).doesNotContain("\"id\":1,\"name\":\"치킨\"");
        assertThat(response).contains("\"id\":2,\"name\":\"피자\"");
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() throws Exception {
        // given
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId1);

        // when
        ResultActions response = requestUpdateCartItemQuantity(member2, cartItemId1, 10);

        // then
        response.andExpect(status().isForbidden());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() throws Exception {
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId1);

        ResultActions response = requestDeleteCartItem(cartItemId1);

        response.andExpect(status().isNoContent());
        int cartItemResponseLength = requestGetCartItems(member).andReturn().getResponse().getContentLength();
        assertThat(cartItemResponseLength).isZero();
    }

    private long getIdFromCreatedResponse(ResultActions result) {
        return Long.parseLong(result.andReturn().getResponse().getHeader("Location").split("/")[2]);
    }

    private ResultActions requestAddCartItem(Member member, CartItemRequest cartItemRequest) throws
        Exception {
        String encodedAuthHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(cartItemRequest);
        return mockMvc.perform(post("/cart-items")
            .header("Authorization", encodedAuthHeader)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRequest));
    }

    private ResultActions requestGetCartItems(Member member) throws Exception {
        String encodedAuthHeader = getEncodedAuthHeader(member);
        return mockMvc.perform(get("/cart-items")
            .header("Authorization", encodedAuthHeader)
            .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private String getEncodedAuthHeader(Member member) {
        return "Basic " + new String(
            getEncoder().encode(String.format("%s:%s", member.getEmail(), member.getPassword()).getBytes()));
    }

    private Long requestAddCartItemAndGetId(Member member, Long productId) throws Exception {
        ResultActions result = requestAddCartItem(member, new CartItemRequest(productId));
        return getIdFromCreatedResponse(result);
    }

    private ResultActions requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) throws Exception {
        String encodedAuthHeader = getEncodedAuthHeader(member);
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        String jsonRequest = new ObjectMapper().writeValueAsString(quantityUpdateRequest);
        return mockMvc.perform(patch("/cart-items/{cartItemId}", cartItemId)
            .header("Authorization", encodedAuthHeader)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRequest));
    }

    private ResultActions requestDeleteCartItem(Long cartItemId) throws Exception {
        String encodedAuthHeader = getEncodedAuthHeader(member);
        return mockMvc.perform(MockMvcRequestBuilders.delete("/cart-items/{cartItemId}", cartItemId)
            .header("Authorization", encodedAuthHeader)
            .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
