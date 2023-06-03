package shop.persistence.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.exception.DatabaseException;
import shop.persistence.entity.CartEntity;
import shop.persistence.entity.detail.CartItemDetail;

import java.util.List;

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
            throw new DatabaseException.IllegalDataException(
                    "잘못된 id 값이 존재합니다. 입력된 상품 id : " + cartEntity.getProductId() +
                            " 회원 id : " + cartEntity.getMemberId()
            );
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

    public CartItemDetail findById(Long id) {
        String sql = "SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, detailRowMapper, id);
        } catch (DataAccessException e) {
            throw new DatabaseException.IllegalDataException(id + "에 해당하는 장바구니 상품이 없습니다.");
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public int updateQuantity(Long id, int quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        return jdbcTemplate.update(sql, quantity, id);
    }

    public void deleteByIds(final List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE id IN (:ids)";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("ids", ids);

        namedParameterJdbcTemplate.update(sql, param);
    }

    public void deleteByMemberIdAndProductIds(Long memberId, List<Long> productIds) {
        String sql = "DELETE FROM cart_item " +
                "WHERE member_id = :memberId AND product_id IN (:productIds)";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("productIds", productIds);

        namedParameterJdbcTemplate.update(sql, param);
    }
}

