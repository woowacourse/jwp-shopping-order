package cart.dto;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.restdocs.snippet.Snippet;

public enum DtoSnippet {

    PRODUCT_REQUEST(ProductRequest.class, prefix -> requestFields()
            .andWithPrefix(
                    prefix,
                    fieldWithPath("name").description("제품 명"),
                    fieldWithPath("price").description("제품 가격"),
                    fieldWithPath("imageUrl").description("제품 이미지 url")
            )
    ),
    PRODUCT_RESPONSE(ProductResponse.class, prefix -> responseFields()
            .andWithPrefix(
                    prefix,
                    fieldWithPath("id").description("제품 id"),
                    fieldWithPath("name").description("제품 명"),
                    fieldWithPath("price").description("제품 가격"),
                    fieldWithPath("imageUrl").description("제품 이미지 url")
            )
    );

    private static final String NO_PREFIX = "";
    private static final String LIST_PREFIX = "[].";

    private final Class<?> clazz;
    private final Function<String, Snippet> toSnippet;

    DtoSnippet(Class<?> clazz, Function<String, Snippet> toSnippet) {
        this.clazz = clazz;
        this.toSnippet = toSnippet;
    }

    public static Snippet of(Object dto) {
        return of(getClassOf(dto), getPrefixOf(dto));
    }

    private static Snippet of(Class<?> clazz, String prefix) {
        return Arrays.stream(values())
                .filter(that -> that.clazz.equals(clazz))
                .map(it -> it.toSnippet.apply(prefix))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 DTO입니다"));
    }

    private static <T> Class<?> getClassOf(T dto) {
        if (dto instanceof List) {
            return ((List<?>)dto).stream()
                    .findFirst()
                    .map(Object::getClass)
                    .orElseThrow(() -> new IllegalArgumentException("빈 리스트입니다"));
        }
        return dto.getClass();
    }

    private static <T> String getPrefixOf(T dto) {
        if (dto instanceof List) {
            return LIST_PREFIX;
        }
        return NO_PREFIX;
    }
}
