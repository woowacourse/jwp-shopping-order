package cart.dao;

import cart.domain.Product;
import cart.exception.ProductNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductDao {

    private static final String TABLE = "product";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String IMAGE_URL = "image_url";
    private static final String STOCK = "stock";
    private static final String ALL_COLUMN = String.join(", ", ID, NAME, PRICE, IMAGE_URL, STOCK);

    private static final RowMapper<Product> rowMapper = (resultSet, rowNum) ->
            new Product(
                    resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getInt(PRICE),
                    resultSet.getString(IMAGE_URL),
                    resultSet.getInt(STOCK)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final Product product) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(product);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Product findById(final long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " where id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new ProductNotFoundException();
        }
    }

    public List<Product> findAll() {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + ";";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(final long id, final Product newProduct) {
        String sql = "UPDATE " + TABLE + " SET name = ?, price = ?, image_url = ?, stock = ? WHERE id = ?;";

        jdbcTemplate.update(sql, newProduct.getName(), newProduct.getPrice(), newProduct.getImageUrl(), newProduct.getStock(), id);
    }

    public void updateStockById(final Long productId, final Integer newQuantity) {
        String sql = "UPDATE " + TABLE + " SET stock = ? WHERE id = ?;";

        jdbcTemplate.update(sql, newQuantity, productId);
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
