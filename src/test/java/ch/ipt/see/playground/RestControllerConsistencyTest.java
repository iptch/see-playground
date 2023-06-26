package ch.ipt.see.playground;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class RestControllerConsistencyTest  {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    private Pattern matchingPattern = Pattern.compile("^[/0-9a-z\\-]+", Pattern.CASE_INSENSITIVE);

    @Test
    void ensurePathNaming() {
        HashSet<String> invalidPaths = new HashSet<String>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntrySet : mapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo mappingInfo = handlerMethodEntrySet.getKey();
            for (String path : mappingInfo.getDirectPaths()) {
                if (!path.matches("^[/0-9a-zA-Z\\-]+$")) {
                    invalidPaths.add(path);
                }
            }
        }
        if (!invalidPaths.isEmpty()) {
            throw new AssertionError("The following endpoint paths contain a character that's not alphanumeric, a forward slashes or a dash (-)\n"+invalidPaths);
        }
    }
}
