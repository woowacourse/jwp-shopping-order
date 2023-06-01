package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CartItemDao cartItemDao;

    public JdbcCartItemRepository(JdbcTemplate jdbcTemplate, CartItemDao cartItemDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartItemDao = cartItemDao;
    }
    
    @Override
    public Optional<CartItem> findById(Long id) {
        try {
            String sql = "SELECT ci.id, ci.quantity, " +
                    "pd.id, pd.name ,pd.price, pd.image_url, " +
                    "mb.id, mb.email, mb.password, mb.money, mb.point FROM cart_item AS ci " +
                    "INNER JOIN product AS pd ON pd.id = ci.product_id " +
                    "INNER JOIN member AS mb ON mb.id = ci.member_id " +
                    "WHERE ci.id = ?";
            CartItem cartItem = jdbcTemplate.queryForObject(sql, new CartItemRowMapper(), id);
            return Optional.ofNullable(cartItem);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        String sql = "SELECT ci.id, ci.quantity, " +
                "pd.id, pd.name ,pd.price, pd.image_url, " +
                "mb.id, mb.email, mb.password, mb.money, mb.point FROM cart_item AS ci " +
                "INNER JOIN product AS pd ON pd.id = ci.product_id " +
                "INNER JOIN member AS mb ON mb.id = ci.member_id " +
                "WHERE ci.member_id = ?";

        return jdbcTemplate.query(sql, new CartItemRowMapper(), memberId);
    }

    @Override
    public Long create(CartItem cartItem) {
        return cartItemDao.insert(new CartItemEntity(cartItem.getMember().getId(), cartItem.getProduct().getId(), cartItem.getQuantity()));
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        cartItemDao.update(cartItem.getId(), cartItem.getQuantity());
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    private static class CartItemRowMapper implements RowMapper<CartItem> {
        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItem(
                    rs.getLong(1),
                    rs.getInt(2),
                    new Product(
                            rs.getLong(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getString(6)
                    ),
                    new Member(
                            rs.getLong(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getInt(10),
                            rs.getInt(11)
                    )
            );
        }
    }
}
