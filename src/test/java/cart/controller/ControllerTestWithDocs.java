package cart.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.apache.groovy.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

import cart.dto.RequestSnippets;
import cart.dto.ResponseSnippets;

@AutoConfigureRestDocs
public abstract class ControllerTestWithDocs {

    protected TestInfo testInfo;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    protected RestDocumentationResultHandler documentationOf() {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
    }

    protected RestDocumentationResultHandler documentationOf(Snippet... snippets) {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                snippets
        );
    }

    protected RestDocumentationResultHandler documentationOf(Object dto, Snippet... snippets) {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                join(
                        snippets,
                        toSnippet(dto)
                )
        );
    }

    protected RestDocumentationResultHandler documentationOf(Object dto, Object otherDto, Snippet... snippets) {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                join(
                        snippets,
                        toSnippet(dto),
                        toSnippet(otherDto)
                )
        );
    }

    private Snippet toSnippet(Object dto) {
        return RequestSnippets.of(dto).or(() -> ResponseSnippets.of(dto))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 DTO입니다"));
    }

    private Snippet[] join(Snippet[] snippets, Snippet... appendSnippets) {
        return Arrays.concat(snippets, appendSnippets);
    }
}
