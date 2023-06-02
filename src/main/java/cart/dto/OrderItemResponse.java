package cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemResponse {

    private final int quantity;
    private final ProductResponse product;
}
