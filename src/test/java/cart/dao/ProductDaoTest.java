package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductDao 은(는)")
@JdbcTest(includeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class ProductDaoTest {

    private static final Product 치킨 = new Product("치킨", 10_000, "www.naver.com");
    private static final Product 피자 = new Product("피자", 15_000, "www.kakao.com");

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_추가한다() {
        // when
        Long actual = productDao.save(치킨);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        Long given = productDao.save(치킨);

        // when
        Optional<Product> actual = productDao.findById(given);

        // then
        assertThat(actual).isPresent();
        상품_검증(actual.get(), 치킨);
    }

    @Test
    void 존재하지_않는_상품_조회시_빈값() {
        // when
        Optional<Product> actual = productDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        productDao.save(치킨);
        productDao.save(피자);

        // when
        List<Product> products = productDao.findAll();

        // then
        상품_검증(products.get(0), 치킨);
        상품_검증(products.get(1), 피자);
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long 치킨_ID = productDao.save(치킨);

        // when
        productDao.deleteById(치킨_ID);

        // then
        assertThat(productDao.findById(치킨_ID)).isEmpty();
    }

    @Test
    void 상품을_변경한다() {
        // given
        Long id = productDao.save(치킨);

        // when
        productDao.updateById(id, 피자);

        // then
        Product actual = productDao.findById(id).get();
        상품_검증(actual, 피자);
    }

    private void 상품_검증(Product actualProduct, Product expectedProduct) {
        assertAll(
                () -> assertThat(actualProduct.getId()).isPositive(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl())
        );
    }
}


