package cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductInfoRequest {

    private final Long productId;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer stock;

    private ProductInfoRequest() {
        this(null, null, null, null, null);
    }
}
