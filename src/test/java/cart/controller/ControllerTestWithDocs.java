package cart.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.apache.groovy.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;

import cart.dto.RequestSnippets;
import cart.dto.ResponseSnippets;

@AutoConfigureRestDocs
public class ControllerTestWithDocs {

    protected TestInfo testInfo;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    protected RestDocumentationResultHandler documentationOf(Snippet... snippets) {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                join(
                        snippets,
                        toSnippet(builderWith(testInfo.getDisplayName()))
                )
        );
    }

    protected RestDocumentationResultHandler documentationOf(Object dto, Snippet... snippets) {
        return document(
                testInfo.getDisplayName(),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                join(
                        snippets,
                        toSnippet(builderWith(dto, testInfo.getDisplayName()))
                )
        );
    }

    private ResourceSnippetParametersBuilder builderWith(String summary) {
        return new ResourceSnippetParametersBuilder()
                .summary(summary)
                .description("");
    }

    private ResourceSnippetParametersBuilder builderWith(Object dto, String summary) {
        return RequestSnippets.resourceBuilderOf(dto)
                .or(() -> ResponseSnippets.resourceBuilderOf(dto))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 DTO입니다"))
                .summary(summary)
                .description("");
    }

    private ResourceSnippet toSnippet(ResourceSnippetParametersBuilder builder) {
        return resource(builder.build());
    }

    private Snippet[] join(Snippet[] snippets, Snippet snippet) {
        return Arrays.concat(snippets, new Snippet[] {snippet});
    }
}
