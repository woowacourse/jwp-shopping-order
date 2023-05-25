package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.CartProductDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartProduct;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartProductSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class CartProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CartProductDao cartProductDao;

    @Test
    void 장바구니에_상품을_추가한다() throws Exception {
        // given
        final Long productId = productDao.saveAndGetId(new Product("pizza1", "pizza1.jpg", 8900L));
        final Long memberId = memberDao.saveAndGetId(new Member("pizza@pizza.com", "password"));
        final CartProductSaveRequest cartProductSaveRequest = new CartProductSaveRequest(productId);
        final String request = objectMapper.writeValueAsString(cartProductSaveRequest);
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        // when
        mockMvc.perform(post("/cart-products")
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        final List<Product> result = cartProductDao.findAllProductByMemberId(memberId);
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getName()).isEqualTo("pizza1"),
                () -> assertThat(result.get(0).getImage()).isEqualTo("pizza1.jpg"),
                () -> assertThat(result.get(0).getPrice()).isEqualTo(8900L)
        );
    }

    @Test
    void 사용자의_장바구니에_담겨있는_모든_상품을_조회한다() throws Exception {
        // given
        final Long productId1 = productDao.saveAndGetId(new Product("pizza1", "pizza1.jpg", 8900L));
        final Long productId2 = productDao.saveAndGetId(new Product("pizza2", "pizza2.jpg", 18900L));
        final Long memberId = memberDao.saveAndGetId(new Member("pizza@pizza.com", "password"));
        cartProductDao.saveAndGetId(new CartProduct(memberId, productId1));
        cartProductDao.saveAndGetId(new CartProduct(memberId, productId2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/cart-products")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].name", is("pizza1")))
                .andExpect(jsonPath("$.products[0].image", is("pizza1.jpg")))
                .andExpect(jsonPath("$.products[0].price", is(8900)))
                .andExpect(jsonPath("$.products[1].name", is("pizza2")))
                .andExpect(jsonPath("$.products[1].image", is("pizza2.jpg")))
                .andExpect(jsonPath("$.products[1].price", is(18900)))
                .andDo(print());
    }

    @Test
    void 장바구니에_담겨있는_상품을_제거한다() throws Exception {
        // given
        final Long productId = productDao.saveAndGetId(new Product("pizza1", "pizza1.jpg", 8900L));
        final Long memberId = memberDao.saveAndGetId(new Member("pizza@pizza.com", "password"));
        cartProductDao.saveAndGetId(new CartProduct(memberId, productId));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        // when
        mockMvc.perform(delete("/cart-products/" + productId)
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertThat(cartProductDao.findAllProductByMemberId(memberId)).isEmpty();
    }
}
