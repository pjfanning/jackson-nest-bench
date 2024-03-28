package org.example.jackson.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import com.fasterxml.jackson.core.util.InternCache;

import java.util.ArrayList;
import java.util.List;

public class InternCacheBench extends BenchmarkLauncher {

    private static final List<String> UUIDs = new ArrayList<>();
    private static final int SIZE = 10000;

    static {
        for (int i = 0; i < SIZE; i++) {
            UUIDs.add(java.util.UUID.randomUUID().toString());
        }
    }

    @Benchmark
    public void intern(Blackhole blackhole) throws Exception {
        for (String UUID : UUIDs) {
            blackhole.consume(InternCache.instance.intern(UUID));
        }
    }

    @Benchmark
    public void internNewLock(Blackhole blackhole) throws Exception {
        for (String UUID : UUIDs) {
            blackhole.consume(InternCacheNewLock.instance.intern(UUID));
        }
    }
}
