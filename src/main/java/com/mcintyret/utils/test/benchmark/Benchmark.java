package com.mcintyret.utils.test.benchmark;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: mcintyret2
 * Date: 10/07/2013
 */

public class Benchmark {

    private static final Map<TimeUnit, String> UNITS = makeUnitsMap();

    private static Map<TimeUnit, String> makeUnitsMap() {
        Map<TimeUnit, String> map = new EnumMap<TimeUnit, String>(TimeUnit.class);
        map.put(TimeUnit.NANOSECONDS, "ns");
        map.put(TimeUnit.MICROSECONDS, "µs");
        map.put(TimeUnit.MILLISECONDS, "ms");
        map.put(TimeUnit.SECONDS, "s");
        map.put(TimeUnit.MINUTES, "m");
        map.put(TimeUnit.HOURS, "hr");
        map.put(TimeUnit.DAYS, "d");

        return Collections.unmodifiableMap(map);
    }

    private final Execution[] executions;

    private final int jvmWarmupIterations;

    private final int iterations;

    private final TimeUnit reportingTimeUnit;

    public static BenchmarkBuilder builder() {
        return new BenchmarkBuilder();
    }

    private Benchmark(Execution[] executions, int jvmWarmupIterations, int iterations, TimeUnit reportingTimeUnit) {
        this.executions = executions;
        this.jvmWarmupIterations = jvmWarmupIterations;
        this.iterations = iterations;
        this.reportingTimeUnit = reportingTimeUnit;
    }

    public void run() throws Throwable {
        String unit = UNITS.get(reportingTimeUnit);
        for (Execution execution : executions) {
            for (int i = 0; i < jvmWarmupIterations; i++) {
                execution.execute();
            }

            long start = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                System.out.println(i);
                execution.execute();
            }

            long time = reportingTimeUnit.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);

            System.out.println(String.format("%s: %d iterations in %d%s. Average: %.2f%s",
                execution.describe(), iterations, time, unit, (time / (double) iterations), unit));
        }
    }

    public static class BenchmarkBuilder {

        private Execution[] executions;

        private int jvmWarmupIterations;

        private int iterations;

        private TimeUnit reportingTimeUnit = TimeUnit.MILLISECONDS;

        public BenchmarkBuilder withExecutions(Execution... executions) {
            this.executions = executions;
            return this;
        }

        public BenchmarkBuilder withJvmWarmupIterations(int jvmWarmupIterations) {
            this.jvmWarmupIterations = jvmWarmupIterations;
            return this;
        }

        public BenchmarkBuilder withIterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        public BenchmarkBuilder withReportingTimeUnit() {
            this.reportingTimeUnit = reportingTimeUnit;
            return this;
        }

        public Benchmark build() {
            return new Benchmark(executions, jvmWarmupIterations, iterations, reportingTimeUnit);
        }

    }

}
