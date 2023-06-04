package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
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
        final Long memberId = result.getLong("member_id");
        final MemberEmail email = new MemberEmail(result.getString("email"));
        final Member member = new Member(memberId, email, null);

        final Long productId = result.getLong("product_id");
        final ProductName productName = new ProductName( result.getString("name"));
        final ProductPrice productPrice = new ProductPrice(result.getInt("price"));
        final ProductImageUrl productImageUrl = new ProductImageUrl(result.getString("image_url"));
        final Product product = new Product(productId, productName, productPrice, productImageUrl);

        final Long cartItemId = result.getLong("cartItem_id");
        final Quantity quantity = new Quantity(result.getInt("quantity"));
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
        final String sql = "SELECT m.id AS member_id, m.email, " +
                "ci.id AS cartItem_id, ci.member_id, ci.quantity, " +
                "p.id AS product_id, p.name, p.price, p.image_url " +
                "FROM cart_item ci " +
                "JOIN member m ON ci.member_id = m.id " +
                "JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT m.id AS member_id, m.email, " +
                "ci.id AS cartItem_id, ci.member_id, ci.quantity, " +
                "p.id AS product_id, p.name, p.price, p.image_url " +
                "FROM cart_item ci " +
                "JOIN member m ON ci.member_id = m.id " +
                "JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.member_id = ?";

        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public List<CartItem> findAllByIds(final List<Long> ids) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        final String sql = "SELECT m.id AS member_id, m.email, " +
                "ci.id AS cartItem_id, ci.member_id, ci.quantity, " +
                "p.id AS product_id, p.name, p.price, p.image_url " +
                "FROM cart_item ci " +
                "JOIN member m ON ci.member_id = m.id " +
                "JOIN product p ON ci.product_id = p.id " +
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

    public void deleteByProductId(final Long productId) {
        final String sql = "DELETE FROM cart_item WHERE product_id = ?";

        jdbcTemplate.update(sql, productId);
    }

    public void update(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        jdbcTemplate.update(sql, cartItem.getQuantityValue(), cartItem.getId());
    }
}
