package com.woowahan.techcourse.product.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"NonAsciiCharacters"})
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 정보_업데이트가_정상적으로_작동한다() {
        // given
        Product product = new Product("name", 1000, "imageUrl");

        // when
        product.updateInfo("newName", 2000, "newImageUrl");

        // then
        assertEquals("newName", product.getName());
        assertEquals(2000, product.getPrice());
        assertEquals("newImageUrl", product.getImageUrl());
    }
}
