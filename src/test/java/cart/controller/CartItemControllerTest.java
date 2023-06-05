package cart.controller;

import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemSaveRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
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
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void 장바구니에_상품을_추가한다() throws Exception {
        // given
        final Product savedProduct = productRepository.save(상품_8900원);
        final Member savedMember = memberRepository.save(사용자1);
        final CartItemSaveRequest cartItemSaveRequest = new CartItemSaveRequest(savedProduct.getId());
        final String request = objectMapper.writeValueAsString(cartItemSaveRequest);
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // when
        mockMvc.perform(post("/cart-items")
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        final List<CartItem> result = cartItemRepository.findAllByMemberId(savedMember.getId());
        assertThat(result).hasSize(1);
    }

    @Test
    void 사용자의_장바구니에_담겨있는_모든_상품을_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_18900원);
        final Member member = memberRepository.save(사용자1);

        cartItemRepository.save(new CartItem(member.getId(), product1));
        cartItemRepository.save(new CartItem(member.getId(), product2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/cart-items")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void 장바구니에_담겨있는_상품을_제거한다() throws Exception {
        // given
        final Product product = productRepository.save(상품_8900원);
        final Member member = memberRepository.save(사용자1);
        cartItemRepository.save(new CartItem(member.getId(), product));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // when
        mockMvc.perform(delete("/cart-items/" + product.getId())
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        assertThat(cartItemRepository.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_변경한다() throws Exception {
        // given
        final Product product = productRepository.save(상품_8900원);
        final Member member = memberRepository.save(사용자1);
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));
        final CartItemQuantityUpdateRequest updateRequest = new CartItemQuantityUpdateRequest(2);

        final String request = objectMapper.writeValueAsString(updateRequest);

        // when
        mockMvc.perform(patch("/cart-items/" + cartItem.getId())
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        final CartItem result = cartItemRepository.findById(cartItem.getId()).get();
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
