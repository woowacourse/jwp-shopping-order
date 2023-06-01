package cart.fixture;

import static cart.fixture.DomainFixture.CHICKEN;

import cart.dto.ProductRequest;

public class DtoFixture {

    public static final ProductRequest CHICKEN_PRODUCT_REQUEST = new ProductRequest(CHICKEN.getName(), CHICKEN.getPrice(),
            CHICKEN.getImageUrl());
}
