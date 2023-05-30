package cart.domain;

import cart.entity.ProductEntity;

public class Product {
	private Long id;
	private String name;
	private int price;
	private String imageUrl;

	public Product(String name, int price, String imageUrl) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Product(Long id, String name, int price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public static Product from(final ProductEntity productEntity) {
		return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(),
			productEntity.getImageUrl());
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
