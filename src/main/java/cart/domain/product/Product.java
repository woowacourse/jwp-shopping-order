package cart.domain.product;

public class Product {
	private Long id;
	private String name;
	private Long price;
	private String imageUrl;

	public Product(String name, Long price, String imageUrl) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Product(Long id, String name, Long price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
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
