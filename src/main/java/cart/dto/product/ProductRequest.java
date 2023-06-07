package cart.dto.product;

import cart.exception.InvalidURLException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductRequest {

    private static final String URL_REGEX = "^https?://(?:www\\.)?[\\w-]+(?:\\.[\\w-]+)+[\\w.,@?^=%&:/~+#-]*$";

    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @Min(value = 0, message = "가격은 음수일 수 없습니다. 유효한 가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "이미지는 필수 입력입니다. 반드시 입력해주세요.")
    private String imageUrl;

    private ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl) {
        isValidUrl(imageUrl);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void isValidUrl(String url) {
        java.util.regex.Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            throw new InvalidURLException();
        }
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
