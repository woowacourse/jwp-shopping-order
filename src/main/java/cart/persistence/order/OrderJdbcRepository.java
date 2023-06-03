package cart.persistence.order;

import cart.application.repository.CouponRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.application.service.order.dto.OrderInfoDto;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final RowMapper<OrderInfoDto> orderInfoDtoRowMapper = (rs, rowNum) ->
            new OrderInfoDto(rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("total_price"),
                    rs.getInt("payment_price"),
                    rs.getInt("point"),
                    rs.getString("created_at")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final OrderedItemRepository orderedItemRepository;

    public OrderJdbcRepository(final JdbcTemplate jdbcTemplate, MemberRepository memberRepository,
                               CouponRepository couponRepository, OrderedItemRepository orderedItemRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
        this.orderedItemRepository = orderedItemRepository;
    }

    @Override
    public Long createOrder(final Order order) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("member_id", order.getMember().getId());
        parameters.addValue("total_price", order.getTotalPrice());
        parameters.addValue("payment_price", order.getPaymentPrice());
        parameters.addValue("point", order.getPoint());
        parameters.addValue("created_at", LocalDateTime.now());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Order> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        List<OrderInfoDto> findOrderInfoDtos = jdbcTemplate.query(sql, orderInfoDtoRowMapper, memberId);
        Member findMember = getMember(memberId);

        return findOrderInfoDtos.stream()
                .map(it -> {
                    List<OrderItem> findOrderItems = orderedItemRepository.findOrderItemsByOrderId(it.getId());

                    return new Order(it.getId(),
                            findMember,
                            findOrderItems,
                            couponRepository.findAllByOrderId(it.getId()),
                            it.getPaymentPrice(),
                            it.getTotalPrice(),
                            it.getPoint(),
                            it.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Order findById(final Long id) {
        final String sql = "SELECT * FROM orders WHERE orders.id = ?";
        OrderInfoDto orderInfoDto = jdbcTemplate.queryForObject(sql, orderInfoDtoRowMapper, id);
        Member member = getMember(orderInfoDto.getMemberId());
        return new Order(orderInfoDto.getId(),
                member,
                orderedItemRepository.findOrderItemsByOrderId(id),
                couponRepository.findAllByOrderId(id),
                orderInfoDto.getPaymentPrice(),
                orderInfoDto.getTotalPrice(),
                orderInfoDto.getPoint(),
                orderInfoDto.getCreatedAt()
        );
    }

    private Member getMember(Long memberId) {
        return memberRepository.findMemberById(memberId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
