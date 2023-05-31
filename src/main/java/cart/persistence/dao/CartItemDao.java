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
        final Long cartItemId = rs.getLong("cartItemId");
        final Long memberId = rs.getLong("memberId");
        final Long productId = rs.getLong("productId");
        final String memberName = rs.getString("memberName");
        final String memberPassword = rs.getString("password");
        final String productName = rs.getString("productName");
        final int productPrice = rs.getInt("price");
        final String productImageUrl = rs.getString("imageUrl");
        final int productQuantity = rs.getInt("quantity");
        return new CartItemDto(cartItemId, memberId, memberName, memberPassword, productId, productName,
            productImageUrl, productPrice, productQuantity);
    };

    public CartItemDao(final JdbcTemplate jdbcTemplate,
                       final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<CartItemDto> findByMemberName(final String memberName) {
        final String sql = "SELECT cart_item.id AS cartItemId, cart_item.member_id AS memberId, "
            + "member.name AS memberName, member.password AS password, product.id AS productId, "
            + "product.name AS productName, product.price AS price, product.image_url AS imageUrl, "
            + "cart_item.quantity AS quantity " +
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
            final String sql = "SELECT cart_item.id AS cartItemId, cart_item.member_id AS memberId, "
                + "member.name AS memberName, member.password AS password, product.id AS productId, "
                + "product.name AS productName, product.price AS price, product.image_url AS imageUrl, "
                + "cart_item.quantity AS quantity " +
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
            + "        INNER JOIN member AS m ON ci.member_id = m.id"
            + "        AND ci.id IN (:ids) AND m.id = :memberId"
            + "    ) cart_item_temp"
            + ")";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);
        return namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public boolean existByMemberNameAndProductId(final String memberName, final Long productId) {
        final String sql = "SELECT COUNT(*) FROM cart_item ci " +
            "INNER JOIN member m ON ci.member_id = m.id " +
            "WHERE m.name = ? and ci.product_id = ?";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, memberName, productId);
        return count > 0;
    }
}

