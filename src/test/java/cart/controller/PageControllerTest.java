package cart.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.cart.Product;
import cart.domain.member.Member;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 관리자_페이지에_접근한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product product2 = productRepository.save(new Product("고양이", "cat.jpg", 1000000L));

        // expect
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute(
                        "products",
                        hasItem(generateProductPropertiesMatcher(product1.getId(), "허브티", "tea.jpg", 1000L))
                ))
                .andExpect(model().attribute(
                        "products",
                        hasItem(generateProductPropertiesMatcher(product2.getId(), "고양이", "cat.jpg", 1000000L))
                ))
                .andDo(print());
    }

    private Matcher<Object> generateProductPropertiesMatcher(
            final Long id,
            final String name,
            final String imageUrl,
            final long price
    ) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("name", is(name)),
                hasProperty("imageUrl", is(imageUrl)),
                hasProperty("price", is(price))
        );
    }

    @Test
    void 세팅_페이지에_접근한다() throws Exception {
        // given
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password2"));

        // expect
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("members", hasSize(2)))
                .andExpect(model().attribute(
                        "members",
                        hasItem(generateMemberPropertiesMatcher(member1.getId(), "pizza1@pizza.com", "password1"))
                ))
                .andExpect(model().attribute(
                        "members",
                        hasItem(generateMemberPropertiesMatcher(member2.getId(), "pizza2@pizza.com", "password2"))
                ))
                .andDo(print());
    }

    private Matcher<Object> generateMemberPropertiesMatcher(
            final Long id,
            final String email,
            final String password
    ) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("email", is(email)),
                hasProperty("password", is(password))
        );
    }
}
