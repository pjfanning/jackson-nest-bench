package org.example.jackson.bench;

import com.fasterxml.jackson.core.StreamReadConstraints;

/**
 * The constraints to use for streaming reads: used to guard against malicious
 * input by preventing processing of "too big" input constructs (values,
 * structures).
 *
 * @since 2.15
 */
public class NewStreamReadConstraints
    implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Default setting for maximum depth: see {@link Builder#maxDepth(int)} for details.
     */
    public static final int DEFAULT_MAX_DEPTH = 1000;

    /**
     * Default setting for maximum number length: see {@link Builder#maxNumberLength(int)} for details.
     */
    public static final int DEFAULT_MAX_NUM_LEN = 1000;

    /**
     * Default setting for maximum string length: see {@link Builder#maxStringLength(int)} for details.
     */
    public static final int DEFAULT_MAX_STRING_LEN = 1_000_000;

    protected final int _maxDepth;
    protected final int _maxNumLen;
    protected final int _maxStringLen;

    private static final NewStreamReadConstraints DEFAULT =
        new NewStreamReadConstraints(DEFAULT_MAX_DEPTH, DEFAULT_MAX_NUM_LEN, DEFAULT_MAX_STRING_LEN);

    public static final class Builder {
        private int maxDepth;
        private int maxNumLen;
        private int maxStringLen;

        /**
         * Sets the maximum depth. The depth is a count of objects and arrays that have not been closed,
         * `{` and `[` respectively.
         *
         * @param maxDepth the maximum depth
         *
         * @return this builder
         * @throws IllegalArgumentException if the maxDepth is set to a negative value
         *
         * @since 2.15
         */
        public Builder maxDepth(final int maxDepth) {
            if (maxDepth < 0) {
                throw new IllegalArgumentException("Cannot set maxDEpth to a negative value");
            }
            this.maxDepth = maxDepth;
            return this;
        }

        /**
         * Sets the maximum number length (in chars or bytes, depending on input context).
         * The default is 1000.
         *
         * @param maxNumLen the maximum number length (in chars or bytes, depending on input context)
         *
         * @return this builder
         * @throws IllegalArgumentException if the maxNumLen is set to a negative value
         *
         * @since 2.15
         */
        public Builder maxNumberLength(final int maxNumLen) {
            if (maxNumLen < 0) {
                throw new IllegalArgumentException("Cannot set maxNumberLength to a negative value");
            }
            this.maxNumLen = maxNumLen;
            return this;
        }

        /**
         * Sets the maximum string length (in chars or bytes, depending on input context).
         * The default is 1,000,000. This limit is not exact, the limit is applied when we increase
         * internal buffer sizes and an exception will happen at sizes greater than this limit. Some
         * text values that are a little bigger than the limit may be treated as valid but no text
         * values with sizes less than or equal to this limit will be treated as invalid.
         * <p>
         *   Setting this value to lower than the {@link #maxNumberLength(int)} is not recommended.
         * </p>
         *
         * @param maxStringLen the maximum string length (in chars or bytes, depending on input context)
         *
         * @return this builder
         * @throws IllegalArgumentException if the maxStringLen is set to a negative value
         *
         * @since 2.15
         */
        public Builder maxStringLength(final int maxStringLen) {
            if (maxStringLen < 0) {
                throw new IllegalArgumentException("Cannot set maxStringLen to a negative value");
            }
            this.maxStringLen = maxStringLen;
            return this;
        }

        Builder() {
            this(DEFAULT_MAX_DEPTH, DEFAULT_MAX_NUM_LEN, DEFAULT_MAX_STRING_LEN);
        }

        Builder(final int maxDepth, final int maxNumLen, final int maxStringLen) {
            this.maxDepth = maxDepth;
            this.maxNumLen = maxNumLen;
            this.maxStringLen = maxStringLen;
        }

        Builder(NewStreamReadConstraints src) {
            maxDepth = src._maxDepth;
            maxNumLen = src._maxNumLen;
            maxStringLen = src._maxStringLen;
        }

        public NewStreamReadConstraints build() {
            return new NewStreamReadConstraints(maxDepth, maxNumLen, maxStringLen);
        }
    }

    /*
    /**********************************************************************
    /* Life-cycle
    /**********************************************************************
     */

    NewStreamReadConstraints(final int maxDepth, final int maxNumLen, final int maxStringLen) {
        _maxDepth = maxDepth;
        _maxNumLen = maxNumLen;
        _maxStringLen = maxStringLen;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static NewStreamReadConstraints defaults() {
        return DEFAULT;
    }

    /**
     * @return New {@link Builder} initialized with settings of this constraints
     *   instance
     */
    public Builder rebuild() {
        return new Builder(this);
    }

    /*
    /**********************************************************************
    /* Accessors
    /**********************************************************************
     */

    /**
     * Accessor for maximum depth.
     * see {@link Builder#maxDepth(int)} for details.
     *
     * @return Maximum allowed depth
     */
    public int getMaxDepth() {
        return _maxDepth;
    }

    /**
     * Accessor for maximum length of numbers to decode.
     * see {@link Builder#maxNumberLength(int)} for details.
     *
     * @return Maximum allowed number length
     */
    public int getMaxNumberLength() {
        return _maxNumLen;
    }

    /**
     * Accessor for maximum length of strings to decode.
     * see {@link Builder#maxStringLength(int)} for details.
     *
     * @return Maximum allowed string length
     */
    public int getMaxStringLength() {
        return _maxStringLen;
    }

    /*
    /**********************************************************************
    /* Convenience methods for validation
    /**********************************************************************
     */

    /**
     * Convenience method that can be used to verify that a floating-point
     * number of specified length does not exceed maximum specified by this
     * constraints object: if it does, a
     * {@link NumberFormatException}
     * is thrown.
     *
     * @param length Length of number in input units
     *
     * @throws NumberFormatException If length exceeds maximum
     */
    public void validateFPLength(int length) throws NumberFormatException
    {
        if (length > _maxNumLen) {
            throw new NumberFormatException(String.format("Number length (%d) exceeds the maximum length (%d)",
                    length, _maxNumLen));
        }
    }

    /**
     * Convenience method that can be used to verify that an integer
     * number of specified length does not exceed maximum specific by this
     * constraints object: if it does, a
     * {@link NumberFormatException}
     * is thrown.
     *
     * @param length Length of number in input units
     *
     * @throws NumberFormatException If length exceeds maximum
     */
    public void validateIntegerLength(int length) throws NumberFormatException
    {
        if (length > _maxNumLen) {
            throw new NumberFormatException(String.format("Number length (%d) exceeds the maximum length (%d)",
                    length, _maxNumLen));
        }
    }

    /**
     * Convenience method that can be used to verify that a String
     * of specified length does not exceed maximum specified by this
     * constraints object: if it does, an
     * {@link IllegalStateException}
     * is thrown.
     *
     * @param length Length of string in input units
     *
     * @throws IllegalStateException If length exceeds maximum
     */
    public void validateStringLength(int length) throws IllegalStateException
    {
        if (length > _maxStringLen) {
            throw new IllegalStateException(String.format("String length (%d) exceeds the maximum length (%d)",
                    length, _maxStringLen));
        }
    }

    /**
     * Convenience method that can be used to verify that the depth
     * does not exceed the maximum specified by this
     * constraints object: if it does, an
     * {@link IllegalStateException}
     * is thrown.
     *
     * @param depth count of unclosed objects and arrays
     *
     * @throws IllegalStateException If depth exceeds maximum
     */
    public void validateDepth(int depth) throws IllegalStateException
    {
        if (depth > _maxDepth) {
            throw new IllegalStateException(String.format("Depth (%d) exceeds the maximum allowed depth (%d)",
                    depth, _maxDepth));
        }
    }
}
