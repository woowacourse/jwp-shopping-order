package cart.ui;

import static cart.fixture.JsonMapper.toJson;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(ProductApiController.class)
@DisplayName("ProductApiController 은(는)")
public class ProductApiControllerTest {

    private static final String API_URL = "/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberDao memberDao;

    @Nested
    class 상품_생성시 {

        @Test
        void 성공한다() throws Exception {
            //given
            ProductRequest request = new ProductRequest("치킨", 10000, "a");
            when(productService.createProduct(any(ProductRequest.class))).thenReturn(1L);

            //when
            ResultActions response = 상품_생성(request);

            //then
            response
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", endsWith("1")));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상품_이름이_null이나_공백이면_예외(String nullOrEmpty) throws Exception {
            // given
            ProductRequest request = new ProductRequest(nullOrEmpty, 10000, "a");

            // when
            ResultActions response = 상품_생성(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        @Test
        void 가격이_null이면_예외() throws Exception {
            // given"
            ProductRequest request = new ProductRequest("치킨", null, "a");

            // when
            ResultActions response = 상품_생성(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 이미지URL이_null이거나_공백이면_예외(String nullOrEmpty) throws Exception {
            // given
            ProductRequest request = new ProductRequest("치킨", 10000, nullOrEmpty);

            // when
            ResultActions response = 상품_생성(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        private ResultActions 상품_생성(ProductRequest request) throws Exception {
            return mockMvc.perform(post(API_URL)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andDo(print());
        }
    }


    @Test
    void 단일_상품을_조회한다() throws Exception {
        //given
        ProductResponse response = new ProductResponse(1L, "김치", 1000, "www.naver.com");
        when(productService.findById(eq(1L)))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get(API_URL + "/{id}", 1L));

        //then
        String body = toJson(response);
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print());
    }

    @Test
    void 모든_상품을_조회한다() throws Exception {
        //given
        List<ProductResponse> response = List.of(
                new ProductResponse(1L, "안성탕면", 1000, "www.naver.com"),
                new ProductResponse(2L, "신라면", 1200, "www.kakao.com")
        );
        when(productService.findAll())
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get(API_URL));

        //then
        String body = toJson(response);
        result
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andDo(print());
    }

    @Nested
    class 상품_수정시 {

        @Test
        void 성공한다() throws Exception {
            //given
            ProductRequest request = new ProductRequest("바뀐김치", 2000, "www.kakao.com");

            //when
            ResultActions result = 상품_수정(request);

            //then
            result
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상품_이름이_null이나_공백이면_예외(String nullOrEmpty) throws Exception {
            // given
            ProductRequest request = new ProductRequest(nullOrEmpty, 10000, "a");

            // when
            ResultActions response = 상품_수정(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        @Test
        void 가격이_null이면_예외() throws Exception {
            // given"
            ProductRequest request = new ProductRequest("치킨", null, "a");

            // when
            ResultActions response = 상품_수정(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 이미지URL이_null이거나_공백이면_예외(String nullOrEmpty) throws Exception {
            // given
            ProductRequest request = new ProductRequest("치킨", 10000, nullOrEmpty);

            // when
            ResultActions response = 상품_수정(request);

            // then
            response.andExpect(status().isUnprocessableEntity());
        }

        private ResultActions 상품_수정(ProductRequest request) throws Exception {
            return mockMvc.perform(put(API_URL + "/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andDo(print());
        }
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete(API_URL + "/{id}", 1L));

        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

