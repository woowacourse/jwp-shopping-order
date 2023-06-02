package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Point;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        return (Long) Objects.requireNonNull(keyHolder.getKeys().get("id"));
    }

    public Optional<CartItem> findById(Long id) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, member.password, member.point, product.id, product.name, product.price, product.image_url, product.stock, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Long memberId = rs.getLong("member_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                int point = rs.getInt("point");
                Long productId = rs.getLong("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                Long cartItemId = rs.getLong("cart_item.id");
                int quantity = rs.getInt("cart_item.quantity");
                int stock = rs.getInt("product.stock");
                Member member = new Member(memberId, email, password, new Point(point));
                Product product = new Product(productId, name, price, imageUrl, stock);
                return new CartItem(cartItemId, quantity, product, member);
            }));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, member.password, member.point, product.id, product.name, product.price, product.image_url, product.stock, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            String password = rs.getString("password");
            int point = rs.getInt("point");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            int stock = rs.getInt("product.stock");
            Member member = new Member(memberId, email, password, new Point(point));
            Product product = new Product(productId, name, price, imageUrl, stock);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Long id = ids.get(i);
                preparedStatement.setLong(1, id);
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

