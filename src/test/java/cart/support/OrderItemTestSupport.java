package cart.support;

import cart.dao.OrderItemDao;
import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemTestSupport {

    private static Long defaultProductId = 1L;
    private static String defaultName = "product";
    private static Integer defaultPrice = 5_000;
    private static String defaultImageUrl = "imageUrl";
    private static Integer defaultQuantity = 1;

    private final OrderItemDao orderItemDao;
    private final OrderHistoryTestSupport orderHistoryTestSupport;

    public OrderItemTestSupport(final OrderItemDao orderItemDao,
                                final OrderHistoryTestSupport orderHistoryTestSupport) {
        this.orderItemDao = orderItemDao;
        this.orderHistoryTestSupport = orderHistoryTestSupport;
    }

    public OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public final class OrderItemBuilder {

        private Long id;
        private OrderHistory orderHistory;
        private Long productId;
        private String name;
        private Integer price;
        private String imageUrl;
        private Integer quantity;

        public OrderItemBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public OrderItemBuilder orderHistory(final OrderHistory orderHistory) {
            this.orderHistory = orderHistory;
            return this;
        }

        public OrderItemBuilder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public OrderItemBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public OrderItemBuilder price(final Integer price) {
            this.price = price;
            return this;
        }

        public OrderItemBuilder imageUrl(final String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public OrderItemBuilder quantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItem build() {
            OrderItem orderItem = make();
            Long orderItemId = orderItemDao.insert(orderItem);
            return new OrderItem(orderItemId, orderItem.getOrderHistory(), orderItem.getProductId(),
                    orderItem.getName(), orderItem.getPrice(), orderItem.getImageUrl(),
                    orderItem.getQuantity());
        }

        public OrderItem make() {
            return new OrderItem(
                    id == null ? null : id,
                    orderHistory == null ? orderHistoryTestSupport.builder().build() : orderHistory,
                    productId == null ? ++defaultProductId : productId,
                    name == null ? defaultName + defaultProductId : name,
                    price == null ? defaultPrice : price,
                    imageUrl == null ? defaultImageUrl : imageUrl,
                    quantity == null ? defaultQuantity : quantity);
        }
    }
}
