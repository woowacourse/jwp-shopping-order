package cart.controller.cart;

import cart.config.auth.guard.basic.MemberArgumentResolver;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.cart.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixture.CartItemFixture.createCartItem;
import static cart.fixture.MemberFixture.createMember;
import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureRestDocs
class CartControllerUnitTest {

    @MockBean
    private CartService cartService;

    @MockBean
    private MemberArgumentResolver memberArgumentResolver;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    void init() throws Exception {
        member = createMember();

        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberRepository.findByEmail(any())).willReturn(member);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
    }

    @DisplayName("유저의 장바구니 품목을 모두 반환한다.")
    @Test
    void find_all_cart_items() throws Exception {
        // given
        List<CartItemResponse> expected = List.of(CartItemResponse.from(createCartItem()));
        when(cartService.findAllCartItems(member)).thenReturn(expected);

        // when
        mockMvc.perform(get("/cart-items")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isOk())
                .andDo(customDocument("find_all_cart_items",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth")
                        ),
                        responseFields(
                                fieldWithPath("[0].cartItemId").description("카트 아이템의 id"),
                                fieldWithPath("[0].quantity").description("카트 아이템의 수량"),
                                fieldWithPath("[0].product.id").description("상품의 id"),
                                fieldWithPath("[0].product.name").description("상품명"),
                                fieldWithPath("[0].product.price").description("상품 가격 (숫자)"),
                                fieldWithPath("[0].product.imageUrl").description("이미지 주소"),
                                fieldWithPath("[0].product.isOnSale").description("세일 여부"),
                                fieldWithPath("[0].product.salePrice").description("상품이 할인되는 가격")
                        )));
    }

    @DisplayName("카트 아이템을 추가한다.")
    @Test
    void add_cart_item() throws Exception {
        // given
        long id = 1L;
        CartItemRequest req = new CartItemRequest(1L);
        when(cartService.add(member, req)).thenReturn(id);

        // when & then
        mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isCreated())
                .andDo(customDocument("add_cart_item",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        requestFields(
                                fieldWithPath("productId").description("상품 id")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("/products/{createdId}")
                        )
                ));
    }

    @DisplayName("카트 아이템의 수량을 변경한다.")
    @Test
    void update_cart_item_quantity() throws Exception {
        // given
        long cartItemId = 1L;
        CartItemQuantityUpdateRequest req = new CartItemQuantityUpdateRequest(10);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cart-items/{id}", cartItemId)
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                ).andExpect(status().isOk())
                .andDo(customDocument("update_quantity",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        pathParameters(
                                parameterWithName("id").description("카트 아이템의 id")
                        )
                ));
    }

    @DisplayName("카트 아이템을 삭제한다.")
    @Test
    void delete_cart_item() throws Exception {
        // given
        long cartItemId = 1L;

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cart-items/{id}", cartItemId)
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isNoContent())
                .andDo(customDocument("delete_cart_item",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        pathParameters(
                                parameterWithName("id").description("카트 아이템의 id")
                        )
                ));
    }

    @DisplayName("카트 아이템을 전부 삭제한다.")
    @Test
    void delete_all_cart_items() throws Exception {
        // given
        long cartItemId = 1L;

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cart-items/{id}", cartItemId)
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isNoContent())
                .andDo(customDocument("delete_all_cart_item",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        )
                ));
    }
}
