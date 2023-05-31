package cart.domain.product;

public class Product {

    private final Long id;
    private final Name name;
    private final ImageUrl image;
    private final Price price;

    public Product(final Name name, final ImageUrl image, final Price price) {
        this(null, name, image, price);
    }

    public Product(final Long id, final Name name, final ImageUrl image, final Price price) {
        validateName(name);
        validateImageUrl(image);
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    private void validateName(final Name name) {
        if (name == null) {
            throw new IllegalArgumentException("상품 이름은 필수 입니다.");
        }
    }

    private void validateImageUrl(final ImageUrl image) {
        if (image == null) {
            throw new IllegalArgumentException("상품 이미지는 필수 입니다.");
        }
    }

    private void validatePrice(final Price price) {
        if (price == null) {
            throw new IllegalArgumentException("상품 가격은 필수 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public ImageUrl getImage() {
        return image;
    }

    public Price getPrice() {
        return price;
    }
}
