package cart.persistence.dao;

import cart.exception.DatabaseException;
import cart.domain.dto.CartItemDetail;
import cart.persistence.entity.CartEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<CartItemDetail> detailRowMapper =
            (rs, rowNum) -> new CartItemDetail(
                    rs.getLong("id"),
                    rs.getInt("cart_item.quantity"),
                    rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getLong("member.id"),
                    rs.getString("member.name"),
                    rs.getString("password")
            );

    public CartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(CartEntity cartEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(cartEntity);

        try {
            return simpleJdbcInsert.executeAndReturnKey(param).longValue();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("잘못된 id 값이 존재합니다. " +
                    "입력된 상품 id : " + cartEntity.getProductId() + " 회원 id : " + cartEntity.getMemberId());
        }
    }

    public List<CartItemDetail> findAllByMemberId(Long memberId) {
        String sql = "SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";

        return jdbcTemplate.query(sql, detailRowMapper, memberId);
    }

    public Optional<CartItemDetail> findById(Long id) {
        String sql = "SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";

        CartItemDetail cartItemDetail = jdbcTemplate.queryForObject(sql, detailRowMapper, id);

        return Optional.of(cartItemDetail);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";

        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public int updateQuantity(Long id, int quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        return jdbcTemplate.update(sql, quantity, id);
    }

    public int deleteByMemberIdAndCartItemIds(final Long memberId, final List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE cart_item.id IN "
                + " (SELECT cart_item.id FROM cart_item " +
                "JOIN member ON cart_item.member_id = member.id AND " +
                "cart_item.id in (:ids) AND " +
                "member.id = (:memberId))";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);

        return namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
}

