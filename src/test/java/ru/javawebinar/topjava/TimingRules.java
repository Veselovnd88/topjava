package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimingRules {

    private static final String DELIM = "--------------------------------";

    private static final Logger log = LoggerFactory.getLogger("result");

    private static final StringBuilder results = new StringBuilder();


    public static final Stopwatch stopWatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-75s %7d", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource summary = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n" + DELIM +
                    "\nTest Duration, ms" +
                    "\n" + DELIM + "\n" + results + DELIM + "\n");
        }
    };
}
