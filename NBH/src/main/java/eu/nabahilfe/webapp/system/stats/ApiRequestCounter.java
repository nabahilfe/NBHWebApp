package eu.nabahilfe.webapp.system.stats;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Service
public class ApiRequestCounter {

    private final ConcurrentHashMap<ApiRequestStatisticKey, LongAdder> counters = new ConcurrentHashMap<>();

    public void increment(String httpMethod, String urlPattern, int statusCode) {
        ApiRequestStatisticKey key = new ApiRequestStatisticKey(LocalDate.now(), httpMethod, urlPattern, statusCode);
        counters.computeIfAbsent(key, ignored -> new LongAdder()).increment();
    }

    /**
     * Nimmt die aktuellen Counter aus dem Map-Snapshot.
     *
     * Neue Requests können währenddessen weiterhin
     * gezählt werden.
     */
    public Map<ApiRequestStatisticKey, Long> drain() {
        Map<ApiRequestStatisticKey, Long> result = new HashMap<>();
        counters.forEach((key, counter) -> {
            long count = counter.sumThenReset();
            if (count > 0) {
                result.put(key, count);
            }
        });

        return result;
    }
}