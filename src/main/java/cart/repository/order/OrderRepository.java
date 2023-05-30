package cart.repository.order;

import cart.dao.coupon.CouponHistoryDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemHistoryDao;
import cart.domain.history.CouponHistory;
import cart.domain.history.OrderHistory;
import cart.domain.history.ProductHistory;
import cart.domain.member.Member;
import cart.dto.order.OrderResponse;
import cart.entity.coupon.CouponHistoryEntity;
import cart.entity.order.OrderItemHistoryEntity;
import cart.entity.order.OrderTableEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemHistoryDao orderItemHistoryDao;
    private final CouponHistoryDao couponHistoryDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemHistoryDao orderItemHistoryDao, final CouponHistoryDao couponHistoryDao) {
        this.orderDao = orderDao;
        this.orderItemHistoryDao = orderItemHistoryDao;
        this.couponHistoryDao = couponHistoryDao;
    }

    public long save(final Member member, final OrderResponse orderHistory) {
        Long orderTableId = createOrderTable(member, orderHistory.getDeliveryPrice().getDeliveryPrice());

        List<OrderItemHistoryEntity> orderItemHistoryEntities = orderHistory.getProducts().stream()
                .map(i -> new OrderItemHistoryEntity(null, i.getProductId(), i.getProductName(), i.getImgUrl(), i.getPrice(), i.getQuantity(), orderTableId))
                .collect(Collectors.toList());

        orderItemHistoryDao.saveAll(orderItemHistoryEntities);

        List<CouponHistoryEntity> couponHistoryEntities = orderHistory.getCoupons().stream()
                .map(i -> new CouponHistoryEntity(i.getCouponId(), i.getCouponName(), orderTableId))
                .collect(Collectors.toList());

        couponHistoryDao.saveAll(couponHistoryEntities);

        return orderTableId;
    }

    private Long createOrderTable(final Member member, final int deliveryFee) {
        return orderDao.save(member.getId(), deliveryFee);
    }

    public List<OrderHistory> findAllByMemberId(final Long memberId) {
        List<OrderTableEntity> orderEntities = orderDao.findAllOrderEntitiesByMemberId(memberId);
        List<OrderHistory> orders = new ArrayList<>();

        for (OrderTableEntity orderTableEntity : orderEntities) {
            Long orderId = orderTableEntity.getId();
            List<OrderItemHistoryEntity> orderItemHistoryEntities = orderItemHistoryDao.findAllByOrderId(orderId);
            List<CouponHistoryEntity> couponHistoryEntities = couponHistoryDao.findAllByOrderId(orderId);

            List<ProductHistory> productHistories = orderItemHistoryEntities.stream()
                    .map(history -> new ProductHistory(history.getId(), history.getProductName(), history.getImgUrl(), history.getQuantity(), history.getPrice()))
                    .collect(Collectors.toList());

            List<CouponHistory> couponHistories = couponHistoryEntities.stream()
                    .map(history -> new CouponHistory(history.getId(), history.getName()))
                    .collect(Collectors.toList());

            orders.add(new OrderHistory(orderId, productHistories, orderTableEntity.getDeliveryFee(), couponHistories, orderTableEntity.getCreateAt()));
        }

        return orders;
    }

    public OrderHistory findOrderHistory(final Long orderId) {
        OrderTableEntity orderTableEntity = orderDao.findById(orderId);

        List<OrderItemHistoryEntity> orderItemHistoryEntities = orderItemHistoryDao.findAllByOrderId(orderId);
        List<CouponHistoryEntity> couponHistoryEntities = couponHistoryDao.findAllByOrderId(orderId);

        List<ProductHistory> productHistories = orderItemHistoryEntities.stream()
                .map(history -> new ProductHistory(history.getId(), history.getProductName(), history.getImgUrl(), history.getQuantity(), history.getPrice()))
                .collect(Collectors.toList());

        List<CouponHistory> couponHistories = couponHistoryEntities.stream()
                .map(history -> new CouponHistory(history.getId(), history.getName()))
                .collect(Collectors.toList());

        return new OrderHistory(orderId, productHistories, orderTableEntity.getDeliveryFee(), couponHistories, orderTableEntity.getCreateAt());
    }

    public boolean isMemberOrder(final Member member, final Long orderId) {
        OrderTableEntity entity = orderDao.findById(orderId);
        return Objects.equals(entity.getMemberId(), member.getId());
    }
}
