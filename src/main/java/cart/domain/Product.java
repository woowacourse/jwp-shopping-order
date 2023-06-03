package cart.domain;

public class Product {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final boolean isDeleted;

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, false);
    }

    public Product(Long id, String name, int price, String imageUrl, boolean isDeleted) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    private void validate(final String name, final int price) {
        validateName(name);
        validatePrice(price);
    }

    private void validatePrice(final int price) {
        if (price < 1 || price > 10_000_000) {
            throw new IllegalArgumentException("상품 가격은 1원 이상 10,000,000원 이하로 입력해주세요.");
        }
    }

    private void validateName(final String name) {
        if (name.length() < 1 || name.length() > 20) {
            throw new IllegalArgumentException("상품 이름은 1글자 이상 20글자 이하로 입력해주세요.");
        }
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

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
