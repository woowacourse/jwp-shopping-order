package cart.controller;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
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
        final Product savedProduct = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Member savedMember = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItemSaveRequest cartItemSaveRequest = new CartItemSaveRequest(savedProduct.getId());
        final String request = objectMapper.writeValueAsString(cartItemSaveRequest);
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

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
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        cartItemRepository.save(new CartItem(member, product1));
        cartItemRepository.save(new CartItem(member, product2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

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
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member, product));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        // when
        mockMvc.perform(delete("/cart-items/" + cartItem.getId())
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
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final CartItem cartItem = cartItemRepository.save(new CartItem(member, product));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));
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
