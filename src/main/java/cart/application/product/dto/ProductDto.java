package cart.application.product.dto;

import cart.domain.product.Product;
import cart.ui.product.dto.ProductRequest;

public class ProductDto {

	private final Long id;
	private final String name;
	private final Long price;
	private final String imageUrl;

	public ProductDto(final Long id, final String name, final Long price, final String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public static ProductDto of(final Long productId , final ProductRequest productRequest) {
		return new ProductDto(productId, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
	}

	public static ProductDto from(final Product product) {
		return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
