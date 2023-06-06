package cart.dto.order;

import cart.domain.product.Product;
import cart.exception.InvalidURLException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderProductRequest {

    private static final String URL_REGEX = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";

    @NotNull(message = "ID는 필수 입력입니다. 반드시 입력해주세요.")
    @Positive(message = "유효한 ID를 입력해주세요.")
    private Long id;
    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @Min(value = 0, message = "가격은 음수일 수 없습니다. 유효한 가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "이미지는 필수 입력입니다. 반드시 입력해주세요.")
    private String imageUrl;

    private OrderProductRequest() {
    }

    public OrderProductRequest(Long id, String name, int price, String imageUrl) {
        isValidUrl(imageUrl);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void isValidUrl(String url) {
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            throw new InvalidURLException();
        }
    }

    public Product toProduct() {
        return new Product(id, name, price, imageUrl);
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
