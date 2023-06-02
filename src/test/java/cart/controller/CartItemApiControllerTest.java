package cart.controller;

import static cart.fixture.TestFixture.CART_ITEMS_MEMBER_A;
import static cart.fixture.TestFixture.CART_ITEM_치킨_MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_B;
import static cart.fixture.TestFixture.제로콜라;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.CartItemService;
import cart.controller.docs.ControllerTestWithDocs;
import cart.dao.MemberDao;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.fixture.TestFixture;
import cart.ui.CartItemApiController;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(CartItemApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CartItemApiControllerTest extends ControllerTestWithDocs {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CartItemService cartItemService;
    @MockBean
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        when(memberDao.getMemberByEmail(notNull())).thenReturn(Optional.empty());
        when(memberDao.getMemberByEmail(eq(MEMBER_A.getEmail()))).thenReturn(Optional.of(MEMBER_A));
        when(memberDao.getMemberByEmail(eq(MEMBER_B.getEmail()))).thenReturn(Optional.of(MEMBER_B));

        when(cartItemService.findByMember(notNull())).thenReturn(Collections.emptyList());
        when(cartItemService.findByMember(eq(MEMBER_A))).thenReturn(CART_ITEMS_MEMBER_A);
        when(cartItemService.add(eq(MEMBER_A), any())).thenReturn(1L);
    }

    @Test
    void 장바구니_항목_조회() throws Exception {
        List<CartItemResponse> cartItemsResponse = CartItemResponse.of(CART_ITEMS_MEMBER_A);
        String response = objectMapper.writeValueAsString(cartItemsResponse);

        //when
        ResultActions result = mockMvc.perform(get("/cart-items")
                .header(HttpHeaders.AUTHORIZATION, TestFixture.AUTHORIZATION_HEADER_MEMBER_A)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print())
                .andDo(documentationOf(
                        cartItemsResponse,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }

    @Test
    void 장바구니_항목_조회_인증실패() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/cart-items")
                .header(HttpHeaders.AUTHORIZATION, TestFixture.AUTHORIZATION_HEADER_INVALID)
        );

        //then
        result
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(documentationOf());
    }

    @Test
    void 장바구니_항목_추가() throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(제로콜라.getId());
        String request = objectMapper.writeValueAsString(cartItemRequest);

        ResultActions result = mockMvc.perform(post("/cart-items")
                .header(HttpHeaders.AUTHORIZATION, TestFixture.AUTHORIZATION_HEADER_MEMBER_A)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        );

        result
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(documentationOf(
                        cartItemRequest,
                        responseHeaders(headerWithName("Location").description("생성된 항목 위치")),
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }

    @Test
    void 장바구니_항목_수량_업데이트() throws Exception {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(10);
        String request = objectMapper.writeValueAsString(quantityUpdateRequest);

        ResultActions result = mockMvc.perform(patch("/cart-items/{id}", CART_ITEM_치킨_MEMBER_A().getId())
                .header(HttpHeaders.AUTHORIZATION, TestFixture.AUTHORIZATION_HEADER_MEMBER_A)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        );

        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf(
                        quantityUpdateRequest,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }

    @Test
    void 장바구니_항목_제거() throws Exception {
        ResultActions result = mockMvc.perform(delete("/cart-items/{id}", CART_ITEM_치킨_MEMBER_A().getId())
                .header(HttpHeaders.AUTHORIZATION, TestFixture.AUTHORIZATION_HEADER_MEMBER_A)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(documentationOf(
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }
}
