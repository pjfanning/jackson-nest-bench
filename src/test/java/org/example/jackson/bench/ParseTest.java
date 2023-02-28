package org.example.jackson.bench;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ParseTest {
    @Test
    void testUtf8Parse() throws Exception {
        final JsonFactory factory = new JsonFactory();
        final String doc = JsonUtil.createDeepNestedDoc(5);
        final ByteQuadsCanonicalizer byteQuadsCanonicalizer = ByteQuadsCanonicalizer.createRoot();
        IOContext ctxt = JsonUtil.createContext(JsonUtil.createContentReference(doc), true);
        try (JsonParser jp = new UTF8StreamJsonParser(ctxt, factory.getParserFeatures(),
                new ByteArrayInputStream(doc.getBytes(StandardCharsets.UTF_8)), null,
                byteQuadsCanonicalizer.makeChild(JsonFactory.Feature.collectDefaults()), new byte[1024], 0, 0, false)) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                System.out.println(jt);
            }
        }
    }
}
