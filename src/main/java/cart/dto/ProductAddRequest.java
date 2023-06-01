package cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductAddRequest {

    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer stock;

    private ProductAddRequest() {
        this(null, null, null, null);
    }
}
