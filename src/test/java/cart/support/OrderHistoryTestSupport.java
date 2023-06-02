package cart.support;

import cart.dao.OrderHistoryDao;
import cart.domain.Member;
import cart.domain.OrderHistory;
import org.springframework.stereotype.Component;

@Component
public class OrderHistoryTestSupport {

    private static Integer defaultOriginalPrice = 10_000;
    private static Integer defaultUsedPoint = 1_000;
    private static Integer defaultOrderPrice = 9_000;

    private final OrderHistoryDao orderHistoryDao;
    private final MemberTestSupport memberTestSupport;

    public OrderHistoryTestSupport(final OrderHistoryDao orderHistoryDao, final MemberTestSupport memberTestSupport) {
        this.orderHistoryDao = orderHistoryDao;
        this.memberTestSupport = memberTestSupport;
    }

    public OrderHistoryBuilder builder() {
        return new OrderHistoryBuilder();
    }

    public final class OrderHistoryBuilder {

        private Long id;
        private Integer originalPrice;
        private Integer usedPoint;
        private Integer orderPrice;
        private Member member;

        public OrderHistoryBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public OrderHistoryBuilder originalPrice(final Integer originalPrice, final Integer usedPoint,
                                                 final Integer orderPrice) {
            this.originalPrice = originalPrice;
            this.usedPoint = usedPoint;
            this.orderPrice = orderPrice;
            return this;
        }

        public OrderHistoryBuilder member(final Member member) {
            this.member = member;
            return this;
        }

        public OrderHistory build() {
            OrderHistory orderHistory = make();
            Long orderHistoryId = orderHistoryDao.add(orderHistory);
            return new OrderHistory(orderHistoryId, orderHistory.getOriginalPrice(), orderHistory.getUsedPoint(),
                    orderHistory.getOrderPrice(), orderHistory.getMember());
        }

        public OrderHistory make() {
            return new OrderHistory(
                    id == null ? null : id,
                    originalPrice == null ? defaultOriginalPrice : originalPrice,
                    usedPoint == null ? defaultUsedPoint : usedPoint,
                    orderPrice == null ? defaultOrderPrice : orderPrice,
                    member == null ? memberTestSupport.builder().build() : member);
        }
    }
}
