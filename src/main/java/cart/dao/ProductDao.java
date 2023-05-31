package cart.dao;

import cart.dao.rowmapper.ProductRowMapper;
import cart.domain.vo.Quantity;
import cart.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.dao.support.SqlHelper.sqlHelper;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertProduct(ProductEntity productEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", productEntity.getName())
                .addValue("price", productEntity.getMoney())
                .addValue("image_url", productEntity.getImageUrl()))
                .longValue();
    }

    public List<ProductEntity> getAllProducts() {
        String sql = sqlHelper()
                .select().columns("*")
                .from().table("product")
                .toString();

        return jdbcTemplate.query(sql, ProductRowMapper.product);
    }

    public ProductEntity getByProductId(Long productId) {
        String sql = sqlHelper()
                .select().columns("*")
                .from().table("product")
                .where().condition("id = ?")
                .toString();

        return jdbcTemplate.queryForObject(sql, ProductRowMapper.product, productId);
    }

    public void updateProduct(Long productId, ProductEntity productEntity) {
        String sql = sqlHelper()
                .update().table("product")
                .set("name = ?, price = ?, image_url = ?")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql,
                productEntity.getName(),
                productEntity.getMoney(),
                productEntity.getImageUrl(),
                productId);
    }

    public void updateMinusQuantity(Long productId, Quantity quantity) {
        String sql = sqlHelper()
                .update().set("quantity = quantity - ?")
                .from().table("product")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, quantity.getValue(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = sqlHelper()
                .delete()
                .from().table("product")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, productId);
    }
}
