package cart.persistence.dao;

import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.entity.CartEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<CartItemDto> cartItemDtoRowMapper = (rs, rowNum) -> {
        final Long cartItemId = rs.getLong("cart_item.id");
        final Long memberId = rs.getLong("member_id");
        final Long productId = rs.getLong("id");
        final String memberName = rs.getString("member_name");
        final String memberPassword = rs.getString("password");
        final String productName = rs.getString("product_name");
        final int productPrice = rs.getInt("price");
        final String productImageUrl = rs.getString("image_url");
        final int productQuantity = rs.getInt("cart_item.quantity");
        return new CartItemDto(cartItemId, memberId, memberName, memberPassword, productId, productName,
            productImageUrl, productPrice, productQuantity);
    };

    public CartItemDao(final JdbcTemplate jdbcTemplate,
                       final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<CartItemDto> findByMemberName(final String memberName) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.name AS member_name, member.password, "
            + "product.id, product.name AS product_name, product.price, product.image_url, cart_item.quantity " +
            "FROM cart_item " +
            "INNER JOIN member ON cart_item.member_id = member.id " +
            "INNER JOIN product ON cart_item.product_id = product.id " +
            "WHERE member.name = ?";
        return jdbcTemplate.query(sql, cartItemDtoRowMapper, memberName);
    }

    public long insert(final CartEntity cartEntity) {
        final String sql = "INSERT INTO cart_item(member_id, product_id, quantity) VALUES (?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, cartEntity.getMemberId());
            ps.setLong(2, cartEntity.getProductId());
            ps.setLong(3, cartEntity.getQuantity());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<CartItemDto> findById(final Long id) {
        try {
            final String sql = "SELECT cart_item.id, cart_item.member_id, member.name AS member_name, member.password, "
                + "product.id, product.name AS product_name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, cartItemDtoRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int updateQuantity(final Long id, final int quantity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantity, id);
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Long countByIdsAndMemberId(final List<Long> ids, final Long memberId) {
        final String sql = "SELECT COUNT(*) " +
            "FROM cart_item " +
            "INNER JOIN member ON cart_item.member_id = member.id " +
            "INNER JOIN product ON cart_item.product_id = product.id " +
            "WHERE cart_item.id IN (:ids) AND member.id = (:memberId)";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Long.class);
    }

    public int deleteByIdsAndMemberId(final List<Long> ids, final Long memberId) {
        final String sql = "DELETE FROM cart_item AS c "
            + "WHERE c.id "
            + "IN ("
            + "    SELECT cart_item_temp.id"
            + "    FROM"
            + "    ("
            + "        SELECT ci.id AS id FROM cart_item AS ci"
            + "        JOIN member AS m ON ci.member_id = m.id"
            + "        AND ci.id IN (:ids) AND m.id = :memberId"
            + "    ) cart_item_temp"
            + ")";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);
        return namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
}

