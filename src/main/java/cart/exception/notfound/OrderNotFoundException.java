package cart.exception.notfound;

public class OrderNotFoundException extends NotFoundException {
	public OrderNotFoundException(final Long id) {
		super(id, "order");
	}
}
