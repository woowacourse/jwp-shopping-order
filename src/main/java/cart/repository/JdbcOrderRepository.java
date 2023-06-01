package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderDetailDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.entity.OrderDetailEntity;
import cart.entity.OrderEntity;
import cart.exception.MemberException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate, OrderDao orderDao, OrderDetailDao orderDetailDao, MemberRepository memberRepository, OrderDetailRepository orderDetailRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.memberRepository = memberRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<Order> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), memberId);
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Long create(Member member, Integer usedPoint, List<CartItem> cartItems) {
        int totalCost = cartItems.stream()
                .map(CartItem::getProduct)
                .mapToInt(Product::getPrice)
                .sum();

        Long orderId = orderDao.insert(new OrderEntity(member.getId(), totalCost - usedPoint, usedPoint));

        createOrderDetails(cartItems, orderId);
        return orderId;
    }

    private void createOrderDetails(List<CartItem> cartItems, Long orderId) {
        for (CartItem cartItem : cartItems) {
            orderDetailDao.insert(new OrderDetailEntity(
                    orderId,
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getImageUrl(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()));
        }
    }

    private class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Order(
                    rs.getLong("id"),
                    memberRepository.findById(rs.getLong("member_id"))
                            .orElseThrow(MemberException.NotFound::new),
                    orderDetailRepository.findAllByOrderId(rs.getLong("id")),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }
}
