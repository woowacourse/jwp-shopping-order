package cart.infrastructure;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.ProductRepository;
import cart.entity.CartItemEntity;
import cart.exception.MemberException;
import cart.exception.ProductException;
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
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public JdbcCartItemRepository(JdbcTemplate jdbcTemplate, CartItemDao cartItemDao, MemberRepository memberRepository, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartItemDao = cartItemDao;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        try {
            String sql = "SELECT * FROM cart_item WHERE id = ?";
            CartItem cartItem = jdbcTemplate.queryForObject(sql, new CartItemRowMapper(), id);
            return Optional.ofNullable(cartItem);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ?";
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

    private class CartItemRowMapper implements RowMapper<CartItem> {
        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItem(
                    rs.getLong("id"),
                    rs.getInt("quantity"),
                    productRepository.findById(rs.getLong("product_id"))
                            .orElseThrow(ProductException.NotFound::new),
                    memberRepository.findById(rs.getLong("member_id"))
                            .orElseThrow(MemberException.NotFound::new)
            );
        }
    }
}
