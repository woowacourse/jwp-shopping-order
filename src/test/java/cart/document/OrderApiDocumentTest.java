package cart.document;

import cart.WebMvcConfig;
import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.CartOrder;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderCartItemsRequest;
import cart.dto.OrderDto;
import cart.dto.OrderedProductDto;
import cart.ui.MemberArgumentResolver;
import cart.ui.OrderApiController;
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

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PIZZA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private WebMvcConfig webMvcConfig;

    @MockBean
    private MemberDao memberDao;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderApiController(orderService))
                .setCustomArgumentResolvers(new MemberArgumentResolver(memberDao))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 특정_유저가_주문한_모든_주문_조회_문서화() throws Exception {
        // given
        final List<OrderedProductDto> orderedProductDtos = List.of(new OrderedProductDto(CHICKEN.ENTITY, 3),
                new OrderedProductDto(PIZZA.ENTITY, 2));
        final CartOrder cartOrder = new CartOrder(1L, MemberA.ENTITY, 56000L, LocalDateTime.now());
        final OrderDto orderDto = new OrderDto(cartOrder, orderedProductDtos);

        given(memberDao.getMemberByEmail(MemberA.EMAIL)).willReturn(MemberA.ENTITY);
        given(orderService.findAllByMemberId(MemberA.ID))
                .willReturn(List.of(orderDto));
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("orders/getOrders",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("[Basic Auth] 로그인 정보")
                        ),
                        responseFields(
                                fieldWithPath("orders.[].orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                                fieldWithPath("orders.[].orderedDateTime").type(JsonFieldType.STRING).description("주문한 시각"),
                                fieldWithPath("orders.[].products").type(JsonFieldType.ARRAY).description("주문한 상품과 수량"),
                                fieldWithPath("orders.[].products.[].product.id").type(JsonFieldType.NUMBER).description("주문한 상품의 ID"),
                                fieldWithPath("orders.[].products.[].product.name").type(JsonFieldType.STRING).description("주문한 상품의 이름"),
                                fieldWithPath("orders.[].products.[].product.price").type(JsonFieldType.NUMBER).description("주문한 상품의 가격"),
                                fieldWithPath("orders.[].products.[].product.imageUrl").type(JsonFieldType.STRING).description("주문한 상품의 이미지 경로"),
                                fieldWithPath("orders.[].products.[].quantity").type(JsonFieldType.NUMBER).description("주문한 상품의 수량"),
                                fieldWithPath("orders.[].totalPrice").type(JsonFieldType.NUMBER).description("주문한 총 금액")
                        )
                ));
    }

    @Test
    void 특정_유저가_주문한_특정_주문_조회_문서화() throws Exception {
        // given
        final List<OrderedProductDto> orderedProductDtos = List.of(new OrderedProductDto(CHICKEN.ENTITY, 3),
                new OrderedProductDto(PIZZA.ENTITY, 2));
        final CartOrder cartOrder = new CartOrder(1L, MemberA.ENTITY, 56000L, LocalDateTime.now());
        final OrderDto orderDto = new OrderDto(cartOrder, orderedProductDtos);

        given(memberDao.getMemberByEmail(MemberA.EMAIL)).willReturn(MemberA.ENTITY);
        given(orderService.findByCartOrderId(cartOrder.getId()))
                .willReturn(orderDto);
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/orders/{cartOrderId}", cartOrder.getId())
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("orders/getOrder",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("[Basic Auth] 로그인 정보")
                        ),
                        pathParameters(
                                parameterWithName("cartOrderId").description("조회할 주문 ID")
                        ),
                        responseFields(
                                fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                                fieldWithPath("orderedDateTime").type(JsonFieldType.STRING).description("주문한 시각"),
                                fieldWithPath("products").type(JsonFieldType.ARRAY).description("주문한 상품과 수량"),
                                fieldWithPath("products.[].product.id").type(JsonFieldType.NUMBER).description("주문한 상품의 ID"),
                                fieldWithPath("products.[].product.name").type(JsonFieldType.STRING).description("주문한 상품의 이름"),
                                fieldWithPath("products.[].product.price").type(JsonFieldType.NUMBER).description("주문한 상품의 가격"),
                                fieldWithPath("products.[].product.imageUrl").type(JsonFieldType.STRING).description("주문한 상품의 이미지 경로"),
                                fieldWithPath("products.[].quantity").type(JsonFieldType.NUMBER).description("주문한 상품의 수량"),
                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("주문한 총 금액")
                        )
                ));
    }

    @Test
    void 특정_유저의_주문하기_문서화() throws Exception {
        // given
        final OrderCartItemDto oneDto = new OrderCartItemDto(1L, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);
        final OrderCartItemDto twoDto = new OrderCartItemDto(2L, PIZZA.NAME, PIZZA.PRICE, PIZZA.IMAGE_URL);
        final List<OrderCartItemDto> orderCartItemDtos = List.of(oneDto, twoDto);
        final OrderCartItemsRequest request = new OrderCartItemsRequest(orderCartItemDtos);

        given(memberDao.getMemberByEmail(MemberA.EMAIL)).willReturn(MemberA.ENTITY);
        given(orderService.addCartOrder(any(), any()))
                .willReturn(1L);
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders/" + 1L))
                .andDo(document("orders/postOrder",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("[Basic Auth] 로그인 정보")
                        ),
                        requestFields(
                                fieldWithPath("orderCartItems").type(JsonFieldType.ARRAY).description("주문하려는 장바구니 목록"),
                                fieldWithPath("orderCartItems.[].cartItemId").type(JsonFieldType.NUMBER).description("장바구니 ID"),
                                fieldWithPath("orderCartItems.[].orderCartItemName").type(JsonFieldType.STRING).description("주문 시점의 상품 이름"),
                                fieldWithPath("orderCartItems.[].orderCartItemPrice").type(JsonFieldType.NUMBER).description("주문 시점의 상품 가격"),
                                fieldWithPath("orderCartItems.[].orderCartItemImageUrl").type(JsonFieldType.STRING).description("주문 시점의 상품 이미지 경로")
                        )
                )
        );
    }
}
