package cart;

import java.time.LocalDateTime;
import java.util.List;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrdersResponse;
import cart.application.dto.OrderContents;
import cart.application.dto.ProductResponse;
import cart.application.dto.SingleKindDetailedProductResponse;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;

public class TestSource {

    public static OrderContents orderContents1 = new OrderContents(1L, 1000, LocalDateTime.now(), "Pending", "order",
        "https://example.com", 1);
    public static OrderContents orderContents2 = new OrderContents(2L, 1000, LocalDateTime.now(), "Pending", "order",
        "https://example.com", 1);
    public static OrderContents orderContents3 = new OrderContents(3L, 1000, LocalDateTime.now(), "Pending", "order",
        "https://example.com", 1);

    public static GetOrdersResponse ordersResponse1 = new GetOrdersResponse(1, 1,
        3, List.of(orderContents1, orderContents2, orderContents3));

    public static ProductResponse productResponse1 = new ProductResponse(1L, "product", 1000, "https://example.com");

    public static SingleKindDetailedProductResponse singleKindDetailedProductResponse1 = new SingleKindDetailedProductResponse(1, productResponse1);

    public static GetDetailedOrderResponse detailedOrderResponse1 = new GetDetailedOrderResponse(1L,
        LocalDateTime.now(), "Pending", 1000, 500, 50, List.of(singleKindDetailedProductResponse1));

    public static Product product1 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static Product product2 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static Product product3 = new Product(3L, "피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

    public static QuantityAndProduct quantityAndProduct1 = new QuantityAndProduct(1, product1);
    public static QuantityAndProduct quantityAndProduct2 = new QuantityAndProduct(1, product2);
    public static QuantityAndProduct quantityAndProduct3 = new QuantityAndProduct(1, product3);

    public static Member member1 = new Member(1L, "a@a.com", "1234");
    public static Member member2 = new Member(2L, "b@b.com", "1234");

    public static Order order1 = new Order(1L, member1, LocalDateTime.now(), 10000, OrderStatus.PENDING, List.of(quantityAndProduct1));
    public static Order order2 = new Order(2L, member1, LocalDateTime.now(), 20000, OrderStatus.PENDING, List.of(quantityAndProduct2));
    public static Order order3 = new Order(3L, member1, LocalDateTime.now(), 13000, OrderStatus.PENDING, List.of(quantityAndProduct3));

    public static Order orderByMember1 = new Order(4L, member1, LocalDateTime.now(), 13000, OrderStatus.PENDING, List.of(quantityAndProduct3));
}
