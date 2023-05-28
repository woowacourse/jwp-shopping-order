package cart.repository.order;

import cart.dao.coupon.CouponHistoryDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemHistoryDao;
import cart.domain.cart.Cart;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.CouponHistory;
import cart.domain.order.Order;
import cart.domain.order.ProductHistory;
import cart.dto.product.ProductUsingCouponAndSaleResponse;
import cart.entity.coupon.CouponHistoryEntity;
import cart.entity.order.OrderItemHistoryEntity;
import cart.entity.order.OrderTableEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    public long save(final Member member, final Cart cart, final List<Coupon> coupons) {
        Long orderTableId = createOrderTable(member, cart.calculateDeliveryFeeUsingCoupons(coupons));

        List<OrderItemHistoryEntity> orderItemHistoryEntities = makeOrderItemHistoryEntities(cart, coupons, orderTableId);

        List<CouponHistoryEntity> couponHistoryEntities = coupons.stream()
                .map(coupon -> new CouponHistoryEntity(coupon.getId(), coupon.getName(), orderTableId))
                .collect(Collectors.toList());

        orderItemHistoryDao.saveAll(orderItemHistoryEntities);
        couponHistoryDao.saveAll(couponHistoryEntities);

        return orderTableId;
    }

    private Long createOrderTable(final Member member, final int deliveryFee) {
        return orderDao.save(member.getId(), deliveryFee);
    }

    private List<OrderItemHistoryEntity> makeOrderItemHistoryEntities(final Cart cart, final List<Coupon> coupons, final Long orderTableId) {
        List<ProductUsingCouponAndSaleResponse> priceHistories = cart.getProductUsingCouponAndSaleResponse(coupons);
        List<String> productNames = cart.getProductNames();

        List<OrderItemHistoryEntity> orderItemHistoryEntities = new ArrayList<>();
        for (int i = 0; i < priceHistories.size(); i++) {
            ProductUsingCouponAndSaleResponse priceHistory = priceHistories.get(i);
            orderItemHistoryEntities.add(new OrderItemHistoryEntity(priceHistory.getProductId(), priceHistory.getProductId(), productNames.get(i), priceHistory.getDiscountPrice(), orderTableId));
        }
        return orderItemHistoryEntities;
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        List<OrderTableEntity> orderEntities = orderDao.findAllOrderEntitiesByMemberId(memberId);
        List<Order> orders = new ArrayList<>();

        for (OrderTableEntity orderTableEntity : orderEntities) {
            Long orderId = orderTableEntity.getId();
            List<OrderItemHistoryEntity> orderItemHistoryEntities = orderItemHistoryDao.findAllByOrderId(orderId);
            List<CouponHistoryEntity> couponHistoryEntities = couponHistoryDao.findAllByOrderId(orderId);

            List<ProductHistory> productHistories = orderItemHistoryEntities.stream()
                    .map(history -> new ProductHistory(history.getId(), history.getProductName(), history.getPrice()))
                    .collect(Collectors.toList());

            List<CouponHistory> couponHistories = couponHistoryEntities.stream()
                    .map(history -> new CouponHistory(history.getId(), history.getName()))
                    .collect(Collectors.toList());

            orders.add(new Order(orderId, productHistories, orderTableEntity.getDeliveryFee(), couponHistories, orderTableEntity.getCreateAt()));
        }

        return orders;
    }

    public Order findById(final Long orderId) {
        OrderTableEntity orderTableEntity = orderDao.findById(orderId);

        List<OrderItemHistoryEntity> orderItemHistoryEntities = orderItemHistoryDao.findAllByOrderId(orderId);
        List<CouponHistoryEntity> couponHistoryEntities = couponHistoryDao.findAllByOrderId(orderId);

        List<ProductHistory> productHistories = orderItemHistoryEntities.stream()
                .map(history -> new ProductHistory(history.getId(), history.getProductName(), history.getPrice()))
                .collect(Collectors.toList());

        List<CouponHistory> couponHistories = couponHistoryEntities.stream()
                .map(history -> new CouponHistory(history.getId(), history.getName()))
                .collect(Collectors.toList());

        return new Order(orderId, productHistories, orderTableEntity.getDeliveryFee(), couponHistories, orderTableEntity.getCreateAt());
    }
}
