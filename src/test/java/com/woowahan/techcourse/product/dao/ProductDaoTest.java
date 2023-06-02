package com.woowahan.techcourse.product.dao;

import static com.woowahan.techcourse.product.domain.ProductFixture.PRODUCT1;
import static org.assertj.core.api.Assertions.assertThat;

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
    }
}
