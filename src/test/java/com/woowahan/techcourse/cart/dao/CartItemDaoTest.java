package com.woowahan.techcourse.cart.dao;

import static com.woowahan.techcourse.cart.domain.CartItemFixture.CART_ITEM1;
import static com.woowahan.techcourse.cart.domain.CartItemFixture.CART_ITEM2;
import static com.woowahan.techcourse.cart.domain.CartItemFixture.CART_ITEM3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.cart.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings({"NonAsciiCharacters"})
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CartItemDaoTest {

    private final CartItemDao cartItemDao;

    @Autowired
    CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Nested
    class 카트_아이템_저장이_필요하지_않은_테스트 {

        @Test
        void 카트_아이템_추가_테스트() {
            // given
            long cartItemId = cartItemDao.save(CART_ITEM1);

            // when
            // then
            assertThat(cartItemDao.findById(cartItemId)).isPresent();
        }

        @Test
        void 없는_것을_조회하면_빈_결과가_나옴() {
            // given
            long cartItemId = cartItemDao.save(CART_ITEM1);

            // when
            // then
            assertThat(cartItemDao.findById(cartItemId + 1)).isEmpty();
        }

        @Test
        void 없는_것을_제거해도_에러가_발생하지_않음() {
            assertThatCode(() -> cartItemDao.deleteById(1L)).doesNotThrowAnyException();
        }
    }

    @Nested
    class 카트_아이템_저장이_필요한_테스트 {

        private long cartItemId;

        @BeforeEach
        void setCartItem() {
            cartItemId = cartItemDao.save(CART_ITEM1);
        }

        @Test
        void 멤버_id와_카트_아이템_id로_조회하면_잘_나온다() {
            // given
            // when
            Optional<CartItem> result = cartItemDao.findByIdAndMemberId(cartItemId, CART_ITEM1.getMemberId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getMemberId()).isEqualTo(CART_ITEM1.getMemberId());
                softly.assertThat(result.get().getProductId()).isEqualTo(CART_ITEM1.getProductId());
                softly.assertThat(result.get().getQuantity()).isEqualTo(CART_ITEM1.getQuantity());
            });
        }

        @Test
        void 카트_전부_제거_테스트() {
            // given
            long cartItemId2 = cartItemDao.save(CART_ITEM2);
            long cartItemId3 = cartItemDao.save(CART_ITEM3);

            // when
            cartItemDao.deleteAll(CART_ITEM1.getMemberId(),
                    List.of(CART_ITEM1.getProductId(), CART_ITEM2.getProductId(), CART_ITEM3.getProductId()));

            // then
            assertThat(cartItemDao.findById(cartItemId)).isEmpty();
            assertThat(cartItemDao.findById(cartItemId2)).isEmpty();
            assertThat(cartItemDao.findById(cartItemId3)).isEmpty();
        }

        @Test
        void 존재하는_카트_아이템을_조회하면_잘_나온다() {
            Optional<CartItem> result = cartItemDao.findById(cartItemId);

            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getMemberId()).isEqualTo(CART_ITEM1.getMemberId());
                softly.assertThat(result.get().getProductId()).isEqualTo(CART_ITEM1.getProductId());
            });
        }
    }
}
