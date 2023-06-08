package cart.helper;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsHelper {
    private static final OperationRequestPreprocessor REQUEST_PREPROCESSOR = Preprocessors.preprocessRequest(
            Preprocessors.prettyPrint());
    private static final OperationResponsePreprocessor RESPONSE_PREPROCESSOR = Preprocessors.preprocessResponse(
            Preprocessors.prettyPrint());

    public static RestDocumentationResultHandler prettyDocument(String identifier, Snippet... snippets) {
        return document(identifier, REQUEST_PREPROCESSOR, RESPONSE_PREPROCESSOR, snippets);
    }

    public static Attribute field(String key, String value) {
        return new Attribute(key, value);
    }

    public static Attribute constraint(String value) {
        return field("constraint", value);
    }
}
