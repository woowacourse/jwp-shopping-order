package cart.domain.product;

public class Item {

    private final Long id;
    private final Product product;
    private int quantity;

    public Item(final Product product) {
        this(null, product, 1);
    }

    public Item(final Product product, final int quantity) {
        this(null, product, quantity);
    }

    public Item(final Long id, final Product product, final int quantity) {
        validateQuantity(quantity);
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(final int quantity) {
        if (quantity < 1 || quantity > 1000) {
            throw new IllegalArgumentException("상품 수량은 1개 이상 1000개 이하로 입력해주세요.");
        }
    }

    public int calculateItemPrice() {
        return this.product.getPrice() * getQuantity();
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
