package cart.infrastructure.repository.dao;

import cart.infrastructure.entity.ProductEntity;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) ->
            new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );


    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> findById(final long id) {
        String sql = "SELECT * FROM product WHERE id = ?";

        try {
            ProductEntity product = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.of(product);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Long create(final ProductEntity product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void update(final ProductEntity product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getId());
    }

    public void deleteById(final long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
