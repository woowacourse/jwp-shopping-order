package cart.persistence.product;

import cart.application.repository.ProductRepository;
import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) ->
            new Product(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );

    public ProductJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long createProduct(final Product product) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(product);
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Product> findAll() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Optional<Product> findById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        try {
            final Product product = jdbcTemplate.queryForObject(sql, productRowMapper, productId);
            return Optional.of(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getId()
        );
    }

    @Override
    public void deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

}
