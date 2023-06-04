package cart.persistence.repository;

import cart.application.domain.Product;
import cart.application.repository.ProductRepository;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductPersistenceAdapter implements ProductRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductPersistenceAdapter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Product insert(Product product) {
        String sql = "INSERT INTO product (name, price, image_url, point_ratio, point_available) " +
                "VALUES (:name, :price, :imageUrl, :pointRatio, :pointAvailable)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(product);
        namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);
        return findById(keyHolder.getKeyAs(Long.class))
                .orElseThrow(); // TODO: 쿼리 또 한번 날리지 않도록 최적화?
    }


    @Override
    public Optional<Product> findById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", productId);
        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url"),
                rs.getDouble("point_ratio"),
                rs.getBoolean("point_available")
        )).stream().findAny();
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url"),
                rs.getDouble("point_ratio"),
                rs.getBoolean("point_available")
        ));
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name = :name, price = :price, image_url = :image_url," +
                " point_ratio = :point_ratio point_available = :point_available WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl())
                .addValue("point_ratio", product.getPointRatio())
                .addValue("point_available", product.isPointAvailable())
                .addValue("id", product.getId());

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void delete(Long productId) {
        String sql = "DELETE FROM product WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", productId);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }
}
