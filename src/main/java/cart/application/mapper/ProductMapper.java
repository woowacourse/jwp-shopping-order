package cart.application.mapper;

import cart.application.dto.product.ProductRequest;
import cart.application.dto.product.ProductResponse;
import cart.domain.product.Product;

public class ProductMapper {

    public static Product convertProduct(final Product product) {
        return new Product(product.getName(), product.getPrice(),
            product.getImageUrl(), product.isDeleted());
    }

    public static Product convertProduct(final ProductRequest productRequest) {
        return new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl(), false);
    }

    public static ProductResponse convertProductResponse(final Long productId, final Product product) {
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static ProductResponse convertProductResponse(final Product Product) {
        return new ProductResponse(Product.getProductId(), Product.getName(),
            Product.getPrice(), Product.getImageUrl());
    }
}
