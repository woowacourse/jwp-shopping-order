package cart.dao;

import cart.domain.CartItem;
import cart.domain.Product;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Product> productRowMapper = (resultSet, rowNum) -> new Product(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("image"),
            resultSet.getLong("price")
    );

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingColumns("member_id", "product_id")
                .usingGeneratedKeyColumns("id");
    }

    public Long saveAndGetId(final CartItem cartItem) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(cartItem);
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Product> findAllProductByMemberId(final Long memberId) {
        final String sql = "SELECT product.id AS id, name, image, price "
                + "FROM cart_item "
                + "LEFT JOIN product "
                + "ON cart_item.product_id = product.id "
                + "WHERE member_id = ?";
        return jdbcTemplate.query(sql, productRowMapper, memberId);
    }

    public int delete(final Long productId, final Long memberId) {
        final String sql = "DELETE FROM cart_item WHERE product_id = ? AND member_id = ?";
        return jdbcTemplate.update(sql, productId, memberId);
    }
}
