package cart.controller;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.ui.ProductApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProductApiControllerTest {

    private static final String API_URL = "/products";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberDao memberDao;

    @Test
    void 상품_생성_테스트() throws Exception {
        //given
        ProductRequest request = new ProductRequest("치킨", 10000, "a");
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(1L);

        //when
        ResultActions result = mockMvc.perform(post(API_URL)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("1")))
                .andDo(print())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("제품 명"),
                                fieldWithPath("price").description("제품 가격"),
                                fieldWithPath("imageUrl").description("제품 이미지 url")
                        )
                ));
    }

    @Test
    void 상품_단일_조회_테스트() throws Exception {
        //given
        ProductResponse response = new ProductResponse(1L, "김치", 1000, "www.naver.com");
        when(productService.getProductById(eq(1L)))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get(API_URL + "/{id}", 1L));

        //then
        String body = objectMapper.writeValueAsString(response);
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print())
                .andDo(document("product-getById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")),
                        responseFields(
                                fieldWithPath("id").description("제품 id"),
                                fieldWithPath("name").description("제품 명"),
                                fieldWithPath("price").description("제품 가격"),
                                fieldWithPath("imageUrl").description("제품 이미지 url")
                        )
                ));
    }

    @Test
    void 상품_전체_조회_테스트() throws Exception {
        //given
        List<ProductResponse> response = List.of(
                new ProductResponse(1L, "안성탕면", 1000, "www.naver.com"),
                new ProductResponse(2L, "신라면", 1200, "www.kakao.com")
        );
        when(productService.getAllProducts())
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get(API_URL));

        //then
        String body = objectMapper.writeValueAsString(response);
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print())
                .andDo(document("product-getAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("제품 id"),
                                fieldWithPath("[].name").description("제품 명"),
                                fieldWithPath("[].price").description("제품 가격"),
                                fieldWithPath("[].imageUrl").description("제품 이미지 url")
                        )
                ));
    }

    @Test
    void 상품_수정() throws Exception {
        //given
        ProductRequest request = new ProductRequest("바뀐김치", 2000, "www.kakao.com");

        //when
        ResultActions result = mockMvc.perform(put(API_URL + "/{id}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("product-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")),
                        requestFields(
                                fieldWithPath("name").description("제품 명"),
                                fieldWithPath("price").description("제품 가격"),
                                fieldWithPath("imageUrl").description("제품 이미지 url")
                        )
                ));
    }

    @Test
    void 상품_삭제() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete(API_URL + "/{id}", 1L));

        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("product-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        )));
    }
}
