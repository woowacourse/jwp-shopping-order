package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public CartItemDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource,
                       final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_item")
                .usingColumns("member_id", "product_id", "quantity")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final RowMapper<CartItem> cartItemRowMapper = (result, rowNum) -> {
        final Long memberId = result.getLong("member.id");
        final String email = result.getString("email");
        final Member member = new Member(memberId, email, null);

        final Long productId = result.getLong("product.id");
        final String productName = result.getString("name");
        final int productPrice = result.getInt("price");
        final String productImageUrl = result.getString("image_url");
        final Product product = new Product(productId, productName, productPrice, productImageUrl);

        final Long cartItemId = result.getLong("cart_item.id");
        final int quantity = result.getInt("quantity");
        return new CartItem(cartItemId, member, product, quantity);
    };

    public Long insert(final CartItem cartItem) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMemberId())
                .addValue("product_id", cartItem.getProductId())
                .addValue("quantity", cartItem.getQuantityValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<CartItem> findById(final Long id) {
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.member_id = ?";

        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }


    public List<CartItem> findAllByIds(final List<Long> ids) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id IN (:ids)";

        return namedParameterJdbcTemplate.query(sql, parameters, cartItemRowMapper);
    }

    public void delete(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(final List<Long> ids) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        jdbcTemplate.update(sql, cartItem.getQuantityValue(), cartItem.getId());
    }
}
