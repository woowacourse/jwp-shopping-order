package cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {

    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer stock;

    private ProductRequest() {
        this(null, null, null, null);
    }
}
