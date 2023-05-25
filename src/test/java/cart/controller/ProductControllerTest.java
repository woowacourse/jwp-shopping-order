package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장한다() throws Exception {
        // given
        final ProductSaveRequest dto = new ProductSaveRequest("허브티", "tea.jpg", 1000L);
        final String request = objectMapper.writeValueAsString(dto);

        // when
        final MvcResult mvcResult = mockMvc.perform(post("/products")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        final String location = mvcResult.getResponse().getHeader("Location");
        final Long id = Long.parseLong(location.substring(10));
        final Product result = productDao.findById(id).orElseThrow();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("허브티"),
                () -> assertThat(result.getImage()).isEqualTo("tea.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(1000L),
                () -> assertThat(location).isEqualTo("/products/" + result.getId())
        );
    }

    @Test
    void 상품을_수정한다() throws Exception {
        // given
        final Product product = new Product("허브티", "tea.jpg", 1000L);
        final Long id = productDao.saveAndGetId(product);
        final ProductUpdateRequest updateRequestDto = new ProductUpdateRequest("고양이", "cat.jpg", 1000000L);
        final String request = objectMapper.writeValueAsString(updateRequestDto);

        // when
        mockMvc.perform(put("/products/" + id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        final Product result = productDao.findById(id).orElseThrow();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo("고양이"),
                () -> assertThat(result.getImage()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(1000000L)
        );
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        // given
        final Product product = new Product("허브티", "tea.jpg", 1000L);
        final Long id = productDao.saveAndGetId(product);

        // when
        mockMvc.perform(delete("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertThat(productDao.findById(id)).isNotPresent();
    }

    @Test
    void 이름이_100자_이상인_상품_등록을_요청하면_400_BadRequest_를_응답한다() throws Exception {
        // given
        final ProductSaveRequest dto = new ProductSaveRequest("허".repeat(101), "tea.jpg", 1000L);
        final String request = objectMapper.writeValueAsString(dto);

        // expect
        mockMvc.perform(post("/products")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 가격이_음수인_상품_등록을_요청하면_400_BadRequest_를_응답한다() throws Exception {
        // given
        final ProductSaveRequest dto = new ProductSaveRequest("허브티", "tea.jpg", -1L);
        final String request = objectMapper.writeValueAsString(dto);

        // expect
        mockMvc.perform(post("/products")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 등록되지_않은_상품_수정을_요청하면_404_BadRequest_를_응답한다() throws Exception {
        // given
        final ProductUpdateRequest updateRequestDto = new ProductUpdateRequest("고양이", "cat.jpg", 1000000L);
        final String request = objectMapper.writeValueAsString(updateRequestDto);

        // expect
        mockMvc.perform(put("/products/" + Long.MAX_VALUE)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 등록되지_않은_상품_삭제를_요청하면_404_BadRequest_를_응답한다() throws Exception {
        // expect
        mockMvc.perform(delete("/products/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}

