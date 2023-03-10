package org.example.jackson.bench;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.jackson.bench.JsonUtil.createContentReference;
import static org.example.jackson.bench.JsonUtil.createContext;

public class NestingBench extends BenchmarkLauncher {

    private static String DOC;
    private static byte[] DOC_BYTES;
    private final static JsonFactory FACTORY = new JsonFactory();
    private final static CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();
    private final static ByteQuadsCanonicalizer _byteQuadsCanonicalizer = ByteQuadsCanonicalizer.createRoot();
    private final static int DEFAULT_FACTORY_FEATURE_FLAGS = JsonFactory.Feature.collectDefaults();
    private final static ExecutorService THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(10);

    static {
        DOC = JsonUtil.createDeepNestedDoc(1000);
        DOC_BYTES = DOC.getBytes(StandardCharsets.UTF_8);
    }

    @Benchmark
    public void nestedDocReaderParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new ReaderBasedJsonParser(ctxt, FACTORY.getParserFeatures(),
                new StringReader(DOC), null, _rootCharSymbols.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void v1NestedDocReaderParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new V1ReaderBasedJsonParser(ctxt, FACTORY.getParserFeatures(),
                new StringReader(DOC), null, _rootCharSymbols.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void v2NestedDocReaderParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new V2ReaderBasedJsonParser(ctxt, FACTORY.getParserFeatures(),
                new StringReader(DOC), null, _rootCharSymbols.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void nestedDocUtf8Parse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new UTF8StreamJsonParser(ctxt, FACTORY.getParserFeatures(),
                new ByteArrayInputStream(DOC_BYTES), null,
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS), new byte[1024], 0, 0, false)) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void v1NestedDocUtf8Parse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new UTF8StreamJsonParser(ctxt, FACTORY.getParserFeatures(),
                new ByteArrayInputStream(DOC_BYTES), null,
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS), new byte[1024], 0, 0, false)) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void nestedDocDataInputParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new UTF8DataInputJsonParser(ctxt, FACTORY.getParserFeatures(),
                new DataInputStream(new ByteArrayInputStream(DOC_BYTES)), null,
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS), 0)) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    @Benchmark
    public void v1NestedDocDataInputParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (JsonParser jp = new V1UTF8DataInputJsonParser(ctxt, FACTORY.getParserFeatures(),
                new DataInputStream(new ByteArrayInputStream(DOC_BYTES)), null,
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS), 0)) {
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    //@Benchmark -- currently hangs
    public void nestedAsyncParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (NonBlockingJsonParser jp = new NonBlockingJsonParser(ctxt, FACTORY.getParserFeatures(),
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            final Runnable r = () -> {
                try {
                    jp.feedInput(DOC_BYTES, 0, DOC_BYTES.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            THREAD_POOL_EXECUTOR.submit(r);
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }

    //@Benchmark -- currently hangs
    public void v1NestedAsyncParse(Blackhole blackhole) throws Exception {
        IOContext ctxt = createContext(createContentReference(DOC), true);
        try (V1NonBlockingJsonParser jp = new V1NonBlockingJsonParser(ctxt, FACTORY.getParserFeatures(),
                _byteQuadsCanonicalizer.makeChild(DEFAULT_FACTORY_FEATURE_FLAGS))) {
            final Runnable r = () -> {
                try {
                    jp.feedInput(DOC_BYTES, 0, DOC_BYTES.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            THREAD_POOL_EXECUTOR.submit(r);
            JsonToken jt;
            while ((jt = jp.nextToken()) != null) {
                blackhole.consume(jt);
            }
        }
    }
}
