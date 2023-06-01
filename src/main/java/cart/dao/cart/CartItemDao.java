package cart.dao.cart;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.product.Product;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItem> CART_ITEMS_ROW_MAPPER = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        boolean isDiscounted = rs.getBoolean("is_discounted");
        int discountRate = rs.getInt("discount_rate");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        Member member = new Member(memberId, email, "Abcd1234@", Rank.valueOf(rs.getString("rank")), rs.getInt("total_purchase_amount"));
        Product product = new Product(productId, name, price, imageUrl, isDiscounted, discountRate);
        return new CartItem(cartItemId, quantity, product, member);
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.rank, member.total_purchase_amount ,product.id, product.name, product.price, product.image_url, product.is_discounted, product.discount_rate, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDiscounted = rs.getBoolean("is_discounted");
            int discountRate = rs.getInt("discount_rate");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, "Abcd1234@", Rank.valueOf(rs.getString("rank")), rs.getInt("total_purchase_amount"));
            Product product = new Product(productId, name, price, imageUrl, isDiscounted, discountRate);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.rank, member.total_purchase_amount, product.id, product.name, product.price, product.image_url, product.is_discounted, product.discount_rate, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";

        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, CART_ITEMS_ROW_MAPPER);
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }

    public List<CartItem> findByIds(final List<Long> cartItemIds) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.rank, member.total_purchase_amount, product.id, product.name, product.price, product.image_url, product.is_discounted, product.discount_rate, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id IN (:cartItemIds)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cartItemIds", cartItemIds);
        return namedParameterJdbcTemplate.query(sql, parameters, CART_ITEMS_ROW_MAPPER);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public void deleteByIds(List<Long> cartItemIds) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, cartItemIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return cartItemIds.size();
            }
        });
    }
}

