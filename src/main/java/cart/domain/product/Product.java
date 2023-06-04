package cart.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

import cart.domain.monetary.Price;

public class Product {
	private Long id;
	private String name;
	private Price price;
	private String imageUrl;

	public Product(String name, BigDecimal price, String imageUrl) {
		this.name = name;
		this.price = new Price(price);
		this.imageUrl = imageUrl;
	}

	public Product(Long id, String name, BigDecimal price, String imageUrl) {
		this.id = id;
		this.name = name;
		this.price = new Price(price);
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.getAmount();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Product product = (Product)o;
		return Objects.equals(id, product.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
