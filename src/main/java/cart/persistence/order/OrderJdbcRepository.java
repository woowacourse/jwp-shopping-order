package cart.persistence.order;

import cart.application.repository.coupon.CouponRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.domain.point.Point;
import cart.persistence.order.dto.OrderDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CouponRepository couponRepository;
    private final OrderedItemRepository orderedItemRepository;


    public OrderJdbcRepository(
            final JdbcTemplate jdbcTemplate,
            final CouponRepository couponRepository,
            final OrderedItemRepository orderedItemRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.couponRepository = couponRepository;
        this.orderedItemRepository = orderedItemRepository;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    private final RowMapper<OrderDto> orderDtoRowMapper = (rs, rowNum) ->
            new OrderDto(
                    rs.getLong("id"),
                    memberRowMapper.mapRow(rs, rowNum),
                    rs.getInt("payment_price"),
                    new Point(rs.getInt("point")),
                    rs.getTimestamp("created_at")
            );

    @Override
    public Long createOrder(final Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final String sql = "INSERT INTO orders (member_id, total_price, payment_price, point) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, order.getMember().getId());
            ps.setInt(2, order.calculateTotalPrice());
            ps.setInt(3, order.getPaymentPrice());
            ps.setInt(4, order.getPoint().getPoint());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Order> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT o.id, o.member_id, o.total_price, o.payment_price, o.point, o.created_at, " +
                "m.name, m.email, m.password " +
                "FROM orders o " +
                "JOIN member m ON o.member_id = m.id " +
                "WHERE o.member_id = ?";
        List<OrderDto> orderDtos = jdbcTemplate.query(sql, orderDtoRowMapper, memberId);

        return orderDtos.stream()
                .map(orderDto -> new Order(
                        orderDto,
                        couponRepository.findCouponByOrderId(memberId, orderDto.getId()),
                        orderedItemRepository.findOrderItemsByOrderId(orderDto.getId()))
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Order> findByOrderId(final Long memberId, final Long orderId) {
        final String sql = "SELECT o.id, o.member_id, o.total_price, o.payment_price, o.point, o.created_at, " +
                "m.name, m.email, m.password " +
                "FROM orders o " +
                "JOIN member m ON o.member_id = m.id " +
                "WHERE o.id = ?";
        try {
            final OrderDto orderDto = jdbcTemplate.queryForObject(sql, orderDtoRowMapper, orderId);
            Coupons coupons = couponRepository.findCouponByOrderId(memberId, orderId);
            OrderItems orderItems = orderedItemRepository.findOrderItemsByOrderId(orderId);
            return Optional.of(new Order(orderDto, coupons, orderItems));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
