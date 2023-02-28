package org.example.jackson.bench;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecyclers;

public class JsonUtil {
    public final static String createDeepNestedDoc(final int depth) {
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

    public final static IOContext createContext(ContentReference contentRef, boolean resourceManaged) {
        return new IOContext(StreamReadConstraints.defaults(), BufferRecyclers.getBufferRecycler(), contentRef, resourceManaged);
    }

    public final static ContentReference createContentReference(Object contentAccessor) {
        return ContentReference.construct(true, contentAccessor);
    }
}
