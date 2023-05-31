package cart.dao;

import cart.dao.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CartItemDao2 {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM cart_item WHERE member_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<CartItemEntity> findByIds(final List<Long> ids) {
        final String sql = "SELECT * FROM cart_item WHERE id IN (%s) ";

        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                String.format(sql, inSql),
                ids.toArray(),
                rowMapper
        );
    }
}
