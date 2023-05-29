package cart;

import java.time.LocalDateTime;
import java.util.List;

import cart.application.dto.GetDetailedOrderResponse;
import cart.application.dto.GetOrderResponse;
import cart.application.dto.ProductResponse;
import cart.application.dto.SingleKindDetailedProductResponse;

public class TestSource {

    public static GetOrderResponse orderResponse1 = new GetOrderResponse(1L, 1000, LocalDateTime.now(), "order",
        "https://example.com", 1);
    public static GetOrderResponse orderResponse2 = new GetOrderResponse(2L, 1000, LocalDateTime.now(), "order",
        "https://example.com", 1);
    public static GetOrderResponse orderResponse3 = new GetOrderResponse(3L, 1000, LocalDateTime.now(), "order",
        "https://example.com", 1);

    public static ProductResponse productResponse1 = new ProductResponse(1L, "product", 1000, "https://example.com");

    public static SingleKindDetailedProductResponse singleKindDetailedProductResponse1 = new SingleKindDetailedProductResponse(1, productResponse1);

    public static GetDetailedOrderResponse detailedOrderResponse1 = new GetDetailedOrderResponse(1L,
        LocalDateTime.now(), 1000, 500, 50, List.of(singleKindDetailedProductResponse1));
}
