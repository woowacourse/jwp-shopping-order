package cart.domain.order.docs;

import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static cart.fixtures.OrderFixtures.Dooly_Order2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.domain.member.application.MemberService;
import cart.domain.member.persistence.MemberDao;
import cart.domain.order.application.OrderService;
import cart.domain.order.dto.OrderRequest;
import cart.domain.order.dto.OrdersResponse;
import cart.domain.order.presentation.OrderApiController;
import cart.fixtures.MemberFixtures;
import cart.global.config.AuthMember;
import cart.global.config.AuthMemberInterceptor;
import cart.global.config.MemberArgumentResolver;
import cart.global.config.WebMvcConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(OrderApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderApiDocumentTest {

    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private MemberDao memberDao;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderApiController(orderService))
                .addInterceptors(new AuthMemberInterceptor(memberService))
                .setCustomArgumentResolvers(new MemberArgumentResolver())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 특정_유저의_주문_문서화() throws Exception {
        // given
        given(memberDao.selectMemberByEmail(MemberFixtures.Dooly.EMAIL)).willReturn(MemberFixtures.Dooly.ENTITY());
        given(orderService.order(any(AuthMember.class), any(OrderRequest.class)))
                .willReturn(Dooly_Order1.ID);
        OrderRequest request = Dooly_Order1.REQUEST();
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberFixtures.Dooly.EMAIL + ":" + MemberFixtures.Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders/" + Dooly_Order1.ID))
                .andDo(document("orders/order",
                                preprocessRequest(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 정보 (Basic Auth)")
                                ),
                                requestFields(
                                        fieldWithPath("orderCartItemDtos.[].cartItemId").type(JsonFieldType.NUMBER).description("장바구니 상품 아이디"),
                                        fieldWithPath("orderCartItemDtos.[].orderCartItemName").type(JsonFieldType.STRING).description("주문할 당시 장바구니 상품 이름"),
                                        fieldWithPath("orderCartItemDtos.[].orderCartItemPrice").type(JsonFieldType.NUMBER).description("주문할 당시 장바구니 상품 가격"),
                                        fieldWithPath("orderCartItemDtos.[].orderCartItemImageUrl").type(JsonFieldType.STRING).description("주문할 당시 장바구니 상품 이미지")
                                )
                        )
                );
    }

    @Test
    void 특정_유저의_주문_목록_조회_문서화() throws Exception {
        // given
        given(memberDao.selectMemberByEmail(MemberFixtures.Dooly.EMAIL)).willReturn(MemberFixtures.Dooly.ENTITY());
        OrdersResponse ordersResponse = new OrdersResponse(List.of(Dooly_Order1.RESPONSE(), Dooly_Order2.RESPONSE()));
        given(orderService.findOrders(any(AuthMember.class))).willReturn(ordersResponse);
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberFixtures.Dooly.EMAIL + ":" + MemberFixtures.Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("orders/showOrders",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 정보 (Basic Auth)")
                                ),
                                responseFields(
                                        fieldWithPath("orders.[].orderId").type(JsonFieldType.NUMBER).description("주문 아이디"),
                                        fieldWithPath("orders.[].orderedDateTime").type(JsonFieldType.STRING).description("주문한 시간 (패턴 : yyyy-MM-dd HH:mm:ss)"),
                                        fieldWithPath("orders.[].totalPrice").type(JsonFieldType.NUMBER).description("주문한 총 가격"),
                                        fieldWithPath("orders.[].products.[].product.id").type(JsonFieldType.NUMBER).description("주문 상품 아이디"),
                                        fieldWithPath("orders.[].products.[].product.name").type(JsonFieldType.STRING).description("주문 상품 이름"),
                                        fieldWithPath("orders.[].products.[].quantity").type(JsonFieldType.NUMBER).description("주문 상품 수량"),
                                        fieldWithPath("orders.[].products.[].product.price").type(JsonFieldType.NUMBER).description("주문 상품 가격"),
                                        fieldWithPath("orders.[].products.[].product.imageUrl").type(JsonFieldType.STRING).description("주문 상품 이미지 URL")
                                )
                        )
                );
    }

    @Test
    void 특정_주문_조회_문서화() throws Exception {
        // given
        given(orderService.findOrder(any(AuthMember.class), eq(Dooly_Order1.ID))).willReturn(Dooly_Order1.RESPONSE());
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberFixtures.Dooly.EMAIL + ":" + MemberFixtures.Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/orders/{id}", Dooly_Order1.ID)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("orders/showOrder",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 정보 (Basic Auth)")
                        ),
                        pathParameters(
                                parameterWithName("id").description("조회할 주문 아이디")
                        ),
                        responseFields(
                                fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 아이디"),
                                fieldWithPath("orderedDateTime").type(JsonFieldType.STRING).description("주문한 시간 (패턴 : yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총 주문 가격"),
                                fieldWithPath("products.[].product.id").type(JsonFieldType.NUMBER).description("주문 상품 아이디"),
                                fieldWithPath("products.[].product.name").type(JsonFieldType.STRING).description("주문 당시의 상품 이름"),
                                fieldWithPath("products.[].product.price").type(JsonFieldType.NUMBER).description("주문 당시의 상품 가격"),
                                fieldWithPath("products.[].product.imageUrl").type(JsonFieldType.STRING).description("주문 당시의 상품 이미지 URL"),
                                fieldWithPath("products.[].quantity").type(JsonFieldType.NUMBER).description("주문 수량")
                        )
                ));
    }
}
