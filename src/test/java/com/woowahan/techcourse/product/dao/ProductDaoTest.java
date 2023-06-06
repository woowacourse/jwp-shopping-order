package com.woowahan.techcourse.product.dao;

import static com.woowahan.techcourse.product.domain.ProductFixture.PRODUCT1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.product.domain.Product;
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
class ProductDaoTest {

    private final ProductDao productDao;

    @Autowired
    private ProductDaoTest(JdbcTemplate jdbcTemplate) {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Nested
    class 저장이_필요하지_않은_테스트 {

        @Test
        void 상품_추가_테스트() {
            // given
            long productId = productDao.insert(PRODUCT1);

            // when
            // then
            assertThat(productDao.findById(productId)).isPresent();
        }

        @Test
        void 없는_상품을_조회하면_empty가_반환된다() {
            // given
            long productId = 1L;

            // when
            Optional<Product> result = productDao.findById(productId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class 저장이_필요한_테스트 {

        private long productId;

        @BeforeEach
        void setUp() {
            productId = productDao.insert(PRODUCT1);
        }

        @Test
        void 정보가_조회된다() {
            Optional<Product> result = productDao.findById(productId);

            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getName()).isEqualTo(PRODUCT1.getName());
                softly.assertThat(result.get().getPrice()).isEqualTo(PRODUCT1.getPrice());
                softly.assertThat(result.get().getImageUrl()).isEqualTo(PRODUCT1.getImageUrl());
            });
        }

        @Test
        void 정보가_잘_덥데이트_된다() {
            // given
            String name = "상품2";
            int price = 2000;
            String imageUrl = "상품2 이미지";

            // when
            productDao.update(new Product(productId, name, price, imageUrl));

            // then
            assertSoftly(softly -> {
                softly.assertThat(productDao.findById(productId)).isPresent();
                softly.assertThat(productDao.findById(productId).get().getName()).isEqualTo(name);
                softly.assertThat(productDao.findById(productId).get().getPrice()).isEqualTo(price);
                softly.assertThat(productDao.findById(productId).get().getImageUrl()).isEqualTo(imageUrl);
            });
        }
    }
}
