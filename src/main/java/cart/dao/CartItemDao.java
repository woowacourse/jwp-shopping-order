package cart.dao;

import cart.entity.CartItemEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final SimpleJdbcInsert jdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
    }

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT c.id as id, "
                + "c.member_id as member_id, "
                + "m.email as member_email, "
                + "m.password as member_password, "
                + "m.point as member_point, "
                + "p.id as product_id, "
                + "p.name as product_name, "
                + "p.price as product_price, "
                + "p.image_url as product_image_url, "
                + "c.quantity as quantity "
                + "FROM cart_item c "
                + "INNER JOIN product p ON c.product_id = p.id "
                + "INNER JOIN member m on c.member_id = m.id "
                + "WHERE c.member_id = ?";
        try {
            return jdbcTemplate.getJdbcTemplate().query(sql, cartEntityMapper(), memberId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT c.id as id, "
                + "c.member_id as member_id, "
                + "m.email as member_email, "
                + "m.password as member_password, "
                + "m.point as member_point, "
                + "p.id as product_id, "
                + "p.name as product_name, "
                + "p.price as product_price, "
                + "p.image_url as product_image_url, "
                + "c.quantity as quantity "
                + "FROM cart_item c "
                + "INNER JOIN product p ON c.product_id = p.id "
                + "INNER JOIN member m on c.member_id = m.id "
                + "WHERE c.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.getJdbcTemplate().queryForObject(sql, cartEntityMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long create(CartItemEntity cartItemEntity) {
        return jdbcTemplate.executeAndReturnKey(new BeanPropertySqlParameterSource(cartItemEntity)).longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.getJdbcTemplate().update(sql, id);
    }

    public void updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.getJdbcTemplate().update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    private static RowMapper<CartItemEntity> cartEntityMapper() {
        return (rs, rowNum) ->
                new CartItemEntity(
                        rs.getLong("id"),
                        rs.getLong("member_id"),
                        rs.getString("member_email"),
                        rs.getString("member_password"),
                        rs.getInt("member_point"),
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("product_price"),
                        rs.getString("product_image_url"),
                        rs.getInt("quantity")
                );
    }
}

