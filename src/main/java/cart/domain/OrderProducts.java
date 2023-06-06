package cart.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderProducts {

    private final List<OrderProduct> orderProducts;

    public OrderProducts(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public static OrderProducts of(final List<OrderInfo> orderInfos, Map<Long, List<Product>> productsInOrderIds) {
        List<OrderProduct> orderProducts = productsInOrderIds.entrySet().stream()
                .map(entry -> new OrderProduct(findOrderInfoById(orderInfos, entry.getKey()), entry.getValue()))
                .collect(Collectors.toUnmodifiableList());
        return new OrderProducts(orderProducts);
    }

    private static OrderInfo findOrderInfoById(final List<OrderInfo> orderInfos, final long id) {
        return orderInfos.stream()
                .filter(orderInfo -> orderInfo.isIdEqual(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("orderInfos 에 검색하는 order id 가 존재하지 않습니다."));
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
