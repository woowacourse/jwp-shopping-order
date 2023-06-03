package cart.persistence.repository;

import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.application.repository.CartItemRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartItemPersistenceAdapter implements CartItemRepository {

    private static final String JOIN_QUERY =
            "SELECT * FROM cart_item " +
                    "INNER JOIN member ON cart_item.member_id = member.id " +
                    "INNER JOIN product ON cart_item.product_id = product.id ";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemPersistenceAdapter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public CartItem insert(CartItem cartItem) {
        String sql = "INSERT INTO CART_ITEM (member_id, product_id, quantity) VALUES (:member_id, :product_id, :quantity)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantity());
        namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public CartItem findById(Long id) {
        String sql = JOIN_QUERY + "WHERE cart_item.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        List<CartItem> cartItems = namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> {
            Member member = makeMember(rs);
            Product product = makeProduct(rs);
            return makeCartItem(rs, member, product);
        });
        return cartItems.stream()
                .findAny()
                .orElseThrow();
    }

    @Override
    public List<CartItem> findByMemberId(Long id) {
        String sql = JOIN_QUERY + "WHERE member.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> {
            Member member = makeMember(rs);
            Product product = makeProduct(rs);
            return makeCartItem(rs, member, product);
        });
    }

    @Override
    public void update(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = :quantity WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("quantity", cartItem.getQuantity())
                .addValue("id", cartItem.getId());
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        String sql = "DELETE FROM cart_item WHERE member_id = :member_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("member_id", memberId);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    private CartItem makeCartItem(ResultSet rs, Member member, Product product) throws SQLException {
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        return new CartItem(cartItemId, quantity, product, member);
    }

    private Product makeProduct(ResultSet rs) throws SQLException {
        Long productId = rs.getLong("product.id");
        String name = rs.getString("product.name");
        int price = rs.getInt("product.price");
        String imageUrl = rs.getString("product.image_url");
        Double pointRatio = rs.getDouble("product.point_ratio");
        Boolean pointAvailable = rs.getBoolean("product.point_available");
        return new Product(productId, name, price, imageUrl, pointRatio, pointAvailable);
    }

    private Member makeMember(ResultSet rs) throws SQLException {
        Long memberId = rs.getLong("member.id");
        String email = rs.getString("member.email");
        Integer point = rs.getInt("member.point");
        return new Member(memberId, email, null, point);
    }
}
