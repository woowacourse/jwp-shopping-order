package cart.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Product;

public class ProductResponse {
	private Long id;
	private String name;
	private int price;
	private String imageUrl;

	private ProductResponse(Long id, String name, int price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public static ProductResponse of(Product product) {
		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getPrice(),
			product.getImageUrl()
		);
	}

	public static List<ProductResponse> ofProducts(final List<Product> products) {
		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList()
			);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
