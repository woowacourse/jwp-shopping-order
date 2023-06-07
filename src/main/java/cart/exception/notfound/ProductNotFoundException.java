package cart.exception.notfound;

public class ProductNotFoundException extends NotFoundException {
	public ProductNotFoundException(final Long id) {
		super(id, "product");
	}
}
