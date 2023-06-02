package cart.support;

import cart.dao.OrderProductDao;
import cart.domain.OrderHistory;
import cart.domain.OrderProduct;
import org.springframework.stereotype.Component;

@Component
public class OrderProductTestSupport {

    private static Long defaultProductId = 1L;
    private static String defaultName = "product";
    private static Integer defaultPrice = 5_000;
    private static String defaultImageUrl = "imageUrl";
    private static Integer defaultQuantity = 1;

    private final OrderProductDao orderProductDao;
    private final OrderHistoryTestSupport orderHistoryTestSupport;

    public OrderProductTestSupport(final OrderProductDao orderProductDao,
                                   final OrderHistoryTestSupport orderHistoryTestSupport) {
        this.orderProductDao = orderProductDao;
        this.orderHistoryTestSupport = orderHistoryTestSupport;
    }

    public OrderProductBuilder builder() {
        return new OrderProductBuilder();
    }

    public final class OrderProductBuilder {

        private Long id;
        private OrderHistory orderHistory;
        private Long productId;
        private String name;
        private Integer price;
        private String imageUrl;
        private Integer quantity;

        public OrderProductBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public OrderProductBuilder orderHistory(final OrderHistory orderHistory) {
            this.orderHistory = orderHistory;
            return this;
        }

        public OrderProductBuilder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public OrderProductBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public OrderProductBuilder price(final Integer price) {
            this.price = price;
            return this;
        }

        public OrderProductBuilder imageUrl(final String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public OrderProductBuilder quantity(final Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderProduct build() {
            OrderProduct orderProduct = make();
            Long orderProductId = orderProductDao.insert(orderProduct);
            return new OrderProduct(orderProductId, orderProduct.getOrderHistory(), orderProduct.getProductId(),
                    orderProduct.getName(), orderProduct.getPrice(), orderProduct.getImageUrl(),
                    orderProduct.getQuantity());
        }

        public OrderProduct make() {
            return new OrderProduct(
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
