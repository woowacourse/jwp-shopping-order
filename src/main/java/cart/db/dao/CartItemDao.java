package cart.db.dao;

import cart.db.entity.CartItemEntity;
import cart.db.entity.MemberEntity;
import cart.db.entity.ProductEntity;
import cart.exception.ItemException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartItemDao {

    private static final String CART_ITEM_JOIN_MEMBER_SQL = "SELECT cart_item.id, cart_item.member_id, member.email, cart_item.product_id, product.product_name, product.price, product.image_url, cart_item.quantity " +
            "FROM cart_item " +
            "INNER JOIN member ON cart_item.member_id = member.id " +
            "INNER JOIN product ON cart_item.product_id = product.id ";
    private static final String WHERE_MEMBER_ID = "WHERE cart_item.member_id = ?";
    private static final String WHERE_CART_ITEM_ID = "WHERE cart_item.id = ?";
    private static final String WHERE_CART_ITEM_IDS = "WHERE cart_item.id IN (:ids) ";

    private static final RowMapper<CartItemEntity> cartItemRowMapper = ((rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("product_id");
        String name = rs.getString("product_name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("quantity");
        MemberEntity member = new MemberEntity(memberId, email, null);
        ProductEntity product = new ProductEntity(productId, name, price, imageUrl);
        return new CartItemEntity(cartItemId, quantity, product, member);
    });

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = CART_ITEM_JOIN_MEMBER_SQL + WHERE_MEMBER_ID;

        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public Long save(CartItemEntity cartItem) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("product_name", cartItem.getProduct().getProductName())
                .addValue("price", cartItem.getProduct().getPrice())
                .addValue("image_url", cartItem.getProduct().getImageUrl())
                .addValue("quantity", cartItem.getQuantity());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public CartItemEntity findById(Long id) {
        String sql = CART_ITEM_JOIN_MEMBER_SQL + WHERE_CART_ITEM_ID;

        try {
            return jdbcTemplate.queryForObject(sql, cartItemRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemException.CanNotFindCartItem();
        }
    }

    public List<CartItemEntity> findAllByIds(List<Long> ids) {
        String sql = CART_ITEM_JOIN_MEMBER_SQL + WHERE_CART_ITEM_IDS;
        SqlParameterSource sources = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(sql, sources, cartItemRowMapper);
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
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
