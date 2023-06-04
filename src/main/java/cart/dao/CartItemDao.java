package cart.dao;

import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.entity.CartItemEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, cartItemEntityRowMapper(), memberId);
    }

    private RowMapper<CartItemEntity> cartItemEntityRowMapper() {
        return (rs, rowNum) -> new CartItemEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"));
    }

    public Long save(CartItem cartItem) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("member_id", cartItem.getMember().getId());
        parameterSource.addValue("product_id", cartItem.getProduct().getId());
        parameterSource.addValue("quantity", cartItem.getQuantity());

        return jdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public CartItemEntity findById(Long id) {
        String sql = "SELECT * FROM cart_item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, cartItemEntityRowMapper(), id);
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

    public void deleteAll(final CartItems cartItems) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                CartItem cartItem = cartItems.getItems().get(i);
                ps.setLong(1, cartItem.getId());
            }

            @Override
            public int getBatchSize() {
                return cartItems.getItems().size();
            }
        });
    }

    public boolean existsByProductIdAndMemberId(Long productId, Long memberId) {
        String sql = "SELECT COUNT(*) FROM cart_item WHERE product_id = ? AND member_id = ?";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, productId, memberId);

        if (integer == null) {
            return false;
        }

        return integer > 0;
    }

    public List<CartItemEntity> findCartItemsByPage(Long memberId, int size, int page) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ? ORDER BY id DESC LIMIT ? OFFSET ? ";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, cartItemEntityRowMapper(), memberId, size, offset);
    }

    public PageInfo findPageInfo(Long memberId, int size, int page) {
        String sql = "SELECT COUNT(*) as total,"
                + " ? as perPage,"
                + " ? as currentPage,"
                + " CEILING(COUNT(*) / CAST(? AS DECIMAL(10, 2))) as lastPage"
                + " FROM cart_item"
                + " WHERE member_id = ?;";
        return jdbcTemplate.queryForObject(sql, pageInfoRowMapper(), size, page, size, memberId);
    }

    private RowMapper<PageInfo> pageInfoRowMapper() {
        return (rs, rowNum) -> new PageInfo(
                rs.getInt("total"),
                rs.getInt("perPage"),
                rs.getInt("currentPage"),
                rs.getInt("lastPage"));
    }
}

