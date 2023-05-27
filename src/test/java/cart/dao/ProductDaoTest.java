package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.Product;

@JdbcTest
class ProductDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("ids를 통해 일치하는 모든 product를 가져온다.")
    void findByIds() {
        //given
        final List<Long> ids = List.of(1L, 2L, 3L);

        //when
        final List<Product> result = productDao.findByIds(ids);
        final List<Long> savedIds = result.stream()
                .map(Product::getId)
                .collect(Collectors.toUnmodifiableList());
        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(savedIds).containsExactlyInAnyOrderElementsOf(ids)
        );
    }
}
