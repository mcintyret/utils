package com.mcintyret.utils.test.benchmark;

import com.google.common.util.concurrent.Uninterruptibles;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: mcintyret2
 * Date: 10/07/2013
 */

public abstract class Benchmark {

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


    public static BenchmarkBuilder builder() {
        return new BenchmarkBuilder();
    }

    protected final Execution[] executions;

    protected final int jvmWarmupIterations;

    protected final int iterations;

    protected final TimeUnit reportingTimeUnit;

    private Benchmark(Execution[] executions, int jvmWarmupIterations, int iterations, TimeUnit reportingTimeUnit) {
        this.executions = executions;
        this.jvmWarmupIterations = jvmWarmupIterations;
        this.iterations = iterations;
        this.reportingTimeUnit = reportingTimeUnit;
    }

    public abstract void run() throws Throwable;

    private static class SerialBenchmark extends Benchmark {

        private SerialBenchmark(Execution[] executions, int jvmWarmupIterations, int iterations, TimeUnit reportingTimeUnit) {
            super(executions, jvmWarmupIterations, iterations, reportingTimeUnit);
        }

        @Override
        public void run() throws Throwable {
            String unit = UNITS.get(reportingTimeUnit);
            for (Execution execution : executions) {
                for (int i = 0; i < jvmWarmupIterations; i++) {
                    execution.execute();
                }

                long start = System.nanoTime();
                for (int i = 0; i < iterations; i++) {
                    execution.execute();
                }

                long time = reportingTimeUnit.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);

                System.out.println(String.format("%s: %d iterations in %d%s. Average: %.2f%s",
                    execution.describe(), iterations, time, unit, (time / (double) iterations), unit));
            }
            System.out.println("Done");
        }
    }

    private static class ParallelBenchmark extends Benchmark {

        private final ExecutorService executorService;

        private final int threads;

        private ParallelBenchmark(Execution[] executions, int jvmWarmupIterations, int iterations, TimeUnit reportingTimeUnit, int threads) {
            super(executions, jvmWarmupIterations, iterations, reportingTimeUnit);
            this.executorService = Executors.newFixedThreadPool(threads);
            this.threads = threads;
        }


        @Override
        public void run() throws Throwable {
            String unit = UNITS.get(reportingTimeUnit);
            try {
                for (final Execution execution : executions) {
                    for (int i = 0; i < jvmWarmupIterations; i++) {
                        execution.execute();
                    }

                    final CountDownLatch start = new CountDownLatch(1);
                    final CountDownLatch finish = new CountDownLatch(threads);

                    final AtomicReference<Throwable> error = new AtomicReference<>();

                    for (int i = 0; i < threads; i++) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                Uninterruptibles.awaitUninterruptibly(start);
                                try {
                                    for (int j = 0; j < iterations; j++) {
                                        execution.execute();
                                    }
                                } catch (Throwable t) {
                                    error.set(t);
                                } finally {
                                    finish.countDown();
                                }
                            }
                        });
                    }

                    long startTime = System.nanoTime();
                    start.countDown();

                    Uninterruptibles.awaitUninterruptibly(finish);
                    long time = reportingTimeUnit.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

                    if (error.get() != null) {
                        throw new ExecutionException("Error while running execution: " + execution.describe(), error.get());
                    }

                    System.out.println(String.format("%s: %d iterations in %d%s. Average: %.2f%s",
                        execution.describe(), iterations, time, unit, (time / (double) iterations), unit));
                }
                System.out.println("Done");
            } finally {
                executorService.shutdownNow();
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
        }
    }


    public static class BenchmarkBuilder {

        private Execution[] executions;

        private int jvmWarmupIterations;

        private int iterations;

        private int threads;

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

        public BenchmarkBuilder withReportingTimeUnit(TimeUnit reportingTimeUnit) {
            this.reportingTimeUnit = reportingTimeUnit;
            return this;
        }

        public BenchmarkBuilder withThreads(int threads) {
            this.threads = threads;
            return this;
        }

        public Benchmark build() {
            if (threads < 2) {
                return new SerialBenchmark(executions, jvmWarmupIterations, iterations, reportingTimeUnit);
            } else {
                return new ParallelBenchmark(executions, jvmWarmupIterations, iterations, reportingTimeUnit, threads);
            }
        }

    }

}
