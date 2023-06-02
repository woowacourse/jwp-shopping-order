package com.woowahan.techcourse.cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.member.dao.MemberDao;
import com.woowahan.techcourse.member.domain.Member;
import com.woowahan.techcourse.product.dao.ProductDao;
import com.woowahan.techcourse.product.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CartItemDaoTest {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    private long memberId;
    private Member member;
    private long productId;
    private Product product;

    @Autowired
    CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @BeforeEach
    void setMemberAndProduct() {
        memberId = memberDao.addMember(new Member(null, "a@a.com", "1234"));
        member = new Member(memberId, "a@a.com", "1234");
        productId = productDao.createProduct(new Product("hello", 1, "asfd@naver.com"));
        product = new Product(productId, "hello", 1, "asfd@naver.com");
    }

    @Nested
    class 카트_아이템_저장이_필요하지_않은_테스트 {

        @Test
        void 카트_아이템_추가_테스트() {
            // given
            long cartItemId = cartItemDao.save(new CartItem(product, memberId));

            // when
            // then
            assertThat(cartItemDao.findById(cartItemId)).isNotNull();
        }
    }

    @Nested
    class 카트_아이템_저장이_필요한_테스트 {

        @Test
        void 카트_전부_제거_테스트() {
            // given
            long cartItemId = cartItemDao.save(new CartItem(product, memberId));
            long cartItemId2 = cartItemDao.save(new CartItem(product, memberId));
            long cartItemId3 = cartItemDao.save(new CartItem(product, memberId));

            // when
            cartItemDao.deleteAll(memberId, List.of(cartItemId, cartItemId2, cartItemId3));

            // then
            assertThat(cartItemDao.findById(cartItemId)).isNull();
            assertThat(cartItemDao.findById(cartItemId2)).isNull();
            assertThat(cartItemDao.findById(cartItemId3)).isNull();
        }
    }
}
