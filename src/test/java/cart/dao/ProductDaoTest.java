package cart.dao;

import cart.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductDaoTest {

    private static final RowMapper<ProductEntity> ROW_MAPPER = (rs, rowNum) -> new ProductEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");

        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 식별자_값으로_상품을_조회한다() {
        ProductEntity 상품 = 핫도그.ENTITY;
        long productId = saveProduct(상품);

        ProductEntity saved = productDao.getById(productId).orElseGet(null);
        assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(상품);
    }

    @Test
    void 상품을_저장한다() {
        ProductEntity 상품 = 피자.ENTITY;
        Long productId = productDao.insert(상품);

        String sql = "SELECT * FROM product WHERE id = ?";
        ProductEntity product = jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId);

        assertThat(product)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(상품);
    }

    @Test
    void 상품_정보를_갱신한다() {
        long productId = saveProduct(치킨.ENTITY);

        ProductEntity 상품_수정 = new ProductEntity(productId, "수정된 이름", 10, "https://수정된_url");
        productDao.update(상품_수정);

        String sql = "SELECT * FROM product WHERE id = ?";
        ProductEntity product = jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId);
        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(상품_수정);
    }

    @Test
    void 모든_상품을_조회한다() {
        saveProduct(피자.ENTITY);
        saveProduct(치킨.ENTITY);
        saveProduct(핫도그.ENTITY);

        String sql = "SELECT * FROM product";
        List<ProductEntity> products = jdbcTemplate.query(sql, ROW_MAPPER);

        assertThat(products).hasSize(3);
    }

    @Test
    void 식별자_값으로_상품을_삭제한다() {
        ProductEntity 상품 = 피자.ENTITY;
        long productId = saveProduct(상품);
        productDao.deleteById(productId);

        assertThatThrownBy(() -> {
            String sql = "SELECT * FROM product WHERE id = ?";
            jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId);
        }).isInstanceOf(DataAccessException.class);
    }

    private long saveProduct(ProductEntity productEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(productEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }
}
