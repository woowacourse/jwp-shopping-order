package cart.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class ProductDaoTest {

    @Autowired
    ProductDao productDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        //given
        final Boolean exist = jdbcTemplate.queryForObject(
                "SELECT deleted from product WHERE id = ?",
                Boolean.class,
                1L
        );
        assertThat(exist).isFalse();

        //when
        productDao.deleteProduct(1L);
        final Boolean deleted = jdbcTemplate.queryForObject(
                "SELECT deleted from product WHERE id = ?",
                Boolean.class,
                1L
        );

        //then
        assertThat(deleted).isTrue();
    }
}
