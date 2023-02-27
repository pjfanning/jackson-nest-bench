package org.example.jackson.bench;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.util.BufferRecyclers;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.io.StringReader;

public class NestingBench extends BenchmarkLauncher {

    private static String DOC;
    private final static JsonFactory FACTORY = new JsonFactory();
    private final static CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();
    private final static int DEFAULT_FACTORY_FEATURE_FLAGS = JsonFactory.Feature.collectDefaults();

    static {
        DOC = createDeepNestedDoc(1000);
    }

    @Benchmark
    public void nestedDocParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = _createContext(_createContentReference(DOC), true);
        try (JsonParser jp = new ReaderBasedJsonParser(ctxt, FACTORY.getParserFeatures(),
                new StringReader(DOC), null, _rootCharSymbols.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void newNestedDocParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = _createContext(_createContentReference(DOC), true);
        try (JsonParser jp = new NewReaderBasedJsonParser(ctxt, FACTORY.getParserFeatures(),
                new StringReader(DOC), null, _rootCharSymbols.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    private IOContext _createContext(ContentReference contentRef, boolean resourceManaged) {
        return new IOContext(StreamReadConstraints.defaults(), BufferRecyclers.getBufferRecycler(), contentRef, resourceManaged);
    }

    private ContentReference _createContentReference(Object contentAccessor) {
        return ContentReference.construct(true, contentAccessor);
    }

    private static String createDeepNestedDoc(final int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < depth; i++) {
            sb.append("{ \"a\": [");
        }
        sb.append(" \"val\" ");
        for (int i = 0; i < depth; i++) {
            sb.append("]}");
        }
        sb.append("]");
        return sb.toString();
    }
}
