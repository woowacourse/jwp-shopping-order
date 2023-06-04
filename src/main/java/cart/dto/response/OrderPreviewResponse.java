package cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class OrderPreviewResponse {
    private final Long id;
    private final String mainProductName;
    private final String mainProductImage;
    private final Integer extraProductCount;
    @JsonProperty("date")
    private final LocalDateTime createdAt;
    @JsonProperty("price")
    private final Integer finalPrice;

    public OrderPreviewResponse(
            final Long id,
            final String mainProductName,
            final String mainProductImage,
            final Integer extraProductCount,
            final LocalDateTime createdAt,
            final Integer finalPrice
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

    public Integer getExtraProductCount() {
        return extraProductCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }
}
