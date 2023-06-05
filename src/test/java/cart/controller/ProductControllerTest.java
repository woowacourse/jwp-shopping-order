package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.VO.Money;
import cart.domain.cart.Product;
import cart.dto.cart.ProductSaveRequest;
import cart.dto.cart.ProductUpdateRequest;
import cart.repository.ProductRepository;
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
    private ProductRepository productRepository;

    @Test
    void 상품을_전체_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product product2 = productRepository.save(new Product("우가티", "tea.jpg", 20000L));

        // expect
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is("허브티")))
                .andExpect(jsonPath("$[0].imageUrl", is("tea.jpg")))
                .andExpect(jsonPath("$[0].price", is(1000)))
                .andExpect(jsonPath("$[1].id", is(product2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is("우가티")))
                .andExpect(jsonPath("$[1].imageUrl", is("tea.jpg")))
                .andExpect(jsonPath("$[1].price", is(20000)))
                .andDo(print());
    }

    @Test
    void 상품을_단일_조회한다() throws Exception {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));

        // expect
        mockMvc.perform(get("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.name", is("허브티")))
                .andExpect(jsonPath("$.imageUrl", is("tea.jpg")))
                .andExpect(jsonPath("$.price", is(1000)))
                .andDo(print());
    }

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
        final Product result = productRepository.findById(id).orElseThrow();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("허브티"),
                () -> assertThat(result.getImageUrl()).isEqualTo("tea.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(Money.from(1000L)),
                () -> assertThat(location).isEqualTo("/products/" + result.getId())
        );
    }

    @Test
    void 상품을_수정한다() throws Exception {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final ProductUpdateRequest updateRequestDto = new ProductUpdateRequest("고양이", "cat.jpg", 1000000L);
        final String request = objectMapper.writeValueAsString(updateRequestDto);

        // when
        mockMvc.perform(put("/products/" + product.getId())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        final Product result = productRepository.findById(product.getId()).orElseThrow();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(product.getId()),
                () -> assertThat(result.getName()).isEqualTo("고양이"),
                () -> assertThat(result.getImageUrl()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(Money.from(1000000L))
        );
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        // given
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));

        // when
        mockMvc.perform(delete("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertThat(productRepository.findById(product.getId())).isNotPresent();
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
}

