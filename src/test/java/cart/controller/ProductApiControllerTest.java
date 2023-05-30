package cart.controller;

import static cart.fixture.TestFixture.감자튀김;
import static cart.fixture.TestFixture.샐러드;
import static cart.fixture.TestFixture.치킨;
import static cart.fixture.TestFixture.햄버거;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.WebMvcConfig;
import cart.application.ProductService;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.ui.ProductApiController;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProductApiControllerTest extends ControllerTestWithDocs {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WebMvcConfig webMvcConfig;
    @MockBean
    private ProductService productService;

    @Test
    void 상품_생성() throws Exception {
        //given
        ProductRequest request = new ProductRequest(치킨.getName(), 치킨.getPrice().getValue(), 치킨.getImageUrl());
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(치킨.getId());

        //when
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("1")))
                .andDo(print())
                .andDo(documentationOf(request));
    }

    @Test
    void 상품_단일_조회() throws Exception {
        //given
        ProductResponse response = ProductResponse.of(샐러드);
        String body = objectMapper.writeValueAsString(response);
        when(productService.getProductById(eq(1L))).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/products/{id}", 1L));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print())
                .andDo(documentationOf(
                        response,
                        pathParameters(parameterWithName("id").description("상품 아이디"))
                ));
    }

    @Test
    void 상품_전체_조회() throws Exception {
        //given
        List<ProductResponse> response = List.of(
                ProductResponse.of(햄버거),
                ProductResponse.of(감자튀김)
        );
        String body = objectMapper.writeValueAsString(response);
        when(productService.getAllProducts()).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/products"));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print())
                .andDo(documentationOf(response));

    }

    @Test
    void 상품_수정() throws Exception {
        //given
        ProductRequest request = new ProductRequest(치킨.getName(), 30000, 치킨.getImageUrl());

        //when
        ResultActions result = mockMvc.perform(put("/products/{id}", 치킨.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf(
                        request,
                        pathParameters(parameterWithName("id").description("상품 아이디"))
                ));
    }

    @Test
    void 상품_삭제() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/products/{id}", 1L));

        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(documentationOf(
                        pathParameters(parameterWithName("id").description("상품 아이디"))
                ));
    }
}
