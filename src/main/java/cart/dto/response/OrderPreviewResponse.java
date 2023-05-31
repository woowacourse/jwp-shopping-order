package cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class OrderPreviewResponse {
    private final Long id;
    private final String mainProductName;
    private final String mainProductImage;
    private final int extraProductCount;
    @JsonProperty("date")
    private final LocalDateTime createdAt;
    @JsonProperty("price")
    private final int finalPrice;

    public OrderPreviewResponse(
            final Long id,
            final String mainProductName,
            final String mainProductImage,
            final int extraProductCount,
            final LocalDateTime createdAt,
            final int finalPrice
    ) {
        this.id = id;
        this.mainProductName = mainProductName;
        this.mainProductImage = mainProductImage;
        this.extraProductCount = extraProductCount;
        this.createdAt = createdAt;
        this.finalPrice = finalPrice;
    }

    public Long getId() {
        return id;
    }

    public String getMainProductName() {
        return mainProductName;
    }

    public String getMainProductImage() {
        return mainProductImage;
    }

    public int getExtraProductCount() {
        return extraProductCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
