package cart.application.mapper;

import cart.application.dto.product.ProductRequest;
import cart.application.dto.product.ProductResponse;
import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;

public class ProductMapper {

    public static Product convertProduct(final ProductWithId productWithId) {
        return new Product(productWithId.getProduct().getName(), productWithId.getProduct().getPrice(),
            productWithId.getProduct().getImageUrl());
    }

    public static Product convertProduct(final ProductRequest productRequest) {
        return new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
    }

    public static ProductResponse convertProductResponse(final Long productId, final Product product) {
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static ProductResponse convertProductResponse(final ProductWithId productWithId) {
        return new ProductResponse(productWithId.getProductId(), productWithId.getProduct().getName(),
            productWithId.getProduct().getPrice(), productWithId.getProduct().getImageUrl());
    }
}
