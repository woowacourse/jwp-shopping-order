package com.woowahan.techcourse.product.ui.dto;

import com.woowahan.techcourse.product.domain.Product;

public class ProductResponse {

    private final long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductResponse(long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public long getId() {
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
