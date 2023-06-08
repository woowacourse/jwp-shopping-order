package cart.cartitem.infrastructure;

import cart.cartitem.CartItem;
import cart.cartitem.application.CartItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao implements CartItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        final var sql = "SELECT CI.id as id, P.name as name, P.price as original_price, P.image_url as image_irl, " +
                "CI.quantity as quantity, CI.product_id as product_id, CI.member_id as member_id " +
                "from CART_ITEM CI " +
                "inner join PRODUCT P on CI.product_id = P.id " +
                "where CI.member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CartItem(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("original_price"),
                        rs.getInt("quantity"),
                        rs.getString("image_url"),
                        rs.getLong("product_id"),
                        rs.getLong("member_id")
                ),
                memberId);
    }

    @Override
    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMemberId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public CartItem findById(Long id) {
        final var sql = "SELECT CI.id as id, P.name as name, P.price as original_price, P.image_url as image_irl, " +
                "CI.quantity as quantity, CI.product_id as product_id, CI.member_id as member_id " +
                "from CART_ITEM CI " +
                "inner join PRODUCT P on CI.product_id = P.id " +
                "where CI.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new CartItem(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("original_price"),
                rs.getInt("quantity"),
                rs.getString("image_url"),
                rs.getLong("product_id"),
                rs.getLong("member_id")
        ), id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

