/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 */

package org.example.jackson.bench.token;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.util.RequestPayload;

/**
 * Exception type for parsing problems, used when non-well-formed content
 * (content that does not conform to JSON syntax as per specification)
 * is encountered.
 */
public class JsonParseException
    extends StreamReadException
{
    private static final long serialVersionUID = 2L; // 2.7

    // @since 2.15
    public JsonParseException(String msg) {
        this(null, msg, null, null);
    }

    @Override
    public StreamReadException withParser(com.fasterxml.jackson.core.JsonParser jsonParser) {
        return this;
    }

    private static JsonLocation _currentLocation(JsonParser p) {
        return (p == null) ? null : p.currentLocation();
    }

    /**
     * Constructor that uses current parsing location as location, and
     * sets processor (accessible via {@link #getProcessor()}) to
     * specified parser.
     *
     * @param p Parser in use when encountering issue reported
     * @param msg Base exception message to use
     *
     * @since 2.7
     */
    public JsonParseException(JsonParser p, String msg) {
        this(p, msg, _currentLocation(p), null);
    }

    // @since 2.7
    public JsonParseException(JsonParser p, String msg, Throwable rootCause) {
        this(p, msg, _currentLocation(p), rootCause);
    }

    // @since 2.7
    public JsonParseException(JsonParser p, String msg, JsonLocation loc) {
        this(p, msg, loc, null);
    }

    // Canonical constructor
    // @since 2.7
    public JsonParseException(JsonParser p, String msg, JsonLocation loc, Throwable rootCause) {
        super(null, msg, loc, rootCause);
    }

    @Deprecated // since 2.7
    public JsonParseException(String msg, JsonLocation loc) {
        this(null, msg, loc, null);
    }

    @Deprecated // since 2.7
    public JsonParseException(String msg, JsonLocation loc, Throwable rootCause) {
        this(null, msg, loc, rootCause);
    }

    /**
     * Fluent method that may be used to assign originating {@link JsonParser},
     * to be accessed using {@link #getProcessor()}.
     *<p>
     * NOTE: `this` instance is modified and no new instance is constructed.
     *
     * @param p Parser instance to assign to this exception
     *
     * @return This exception instance to allow call chaining
     *
     * @since 2.7
     */
    public JsonParseException withParser(JsonParser p) {
        return this;
    }

    /**
     * Fluent method that may be used to assign payload to this exception,
     * to let recipient access it for diagnostics purposes.
     *<p>
     * NOTE: `this` instance is modified and no new instance is constructed.
     *
     * @param payload Payload to assign to this exception
     *
     * @return This exception instance to allow call chaining
     *
     * @since 2.8
     */
    @Override
    public JsonParseException withRequestPayload(RequestPayload payload) {
        _requestPayload = payload;
        return this;
    }

    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
    public com.fasterxml.jackson.core.JsonParser getProcessor() {
        return super.getProcessor();
    }

    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
    public RequestPayload getRequestPayload() {
        return super.getRequestPayload();
    }

    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
    public String getRequestPayloadAsString() {
        return super.getRequestPayloadAsString();
    }

    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
