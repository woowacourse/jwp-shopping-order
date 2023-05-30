package cart.dao;

import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("member_email");
        Long productId = rs.getLong("product_id");
        String name = rs.getString("product_name");
        BigDecimal price = rs.getBigDecimal("product_price");
        String imageUrl = rs.getString("product_image_url");
        Long cartItemId = rs.getLong("cart_item_id");
        int quantity = rs.getInt("cart_item_quantity");
        MemberEntity member = new MemberEntity(memberId, email, null);
        ProductEntity product = new ProductEntity(productId, name, price, imageUrl);
        return new CartItemEntity(cartItemId, product, member, quantity);
    };
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql =
                "SELECT cart_item.id as cart_item_id, cart_item.quantity as cart_item_quantity, " +
                        "cart_item.member_id as member_id, member.email as member_email, " +
                        "product.id as product_id, product.name as product_name, product.price as product_price, product.image_url as product_image_url, "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id AND product.is_deleted = false " +
                        "WHERE cart_item.member_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(CartItemEntity cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMemberEntity().getId());
            ps.setLong(2, cartItem.getProductEntity().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql =
                "SELECT cart_item.id as cart_item_id, cart_item.quantity as cart_item_quantity, " +
                        "cart_item.member_id as member_id, member.email as member_email, " +
                        "product.id as product_id, product.name as product_name, product.price as product_price, product.image_url as product_image_url, "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id AND product.is_deleted = false " +
                        "WHERE cart_item.id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItemEntity cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

