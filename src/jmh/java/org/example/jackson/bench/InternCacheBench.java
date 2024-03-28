package org.example.jackson.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import com.fasterxml.jackson.core.util.InternCache;

public class InternCacheBench extends BenchmarkLauncher {

    private static final String UUID = java.util.UUID.randomUUID().toString();

    @Benchmark
    public void intern(Blackhole blackhole) throws Exception {
        InternCache.instance.intern(UUID);
    }

    @Benchmark
    public void internNewLock(Blackhole blackhole) throws Exception {
        InternCacheNewLock.instance.intern(UUID);
    }
}
