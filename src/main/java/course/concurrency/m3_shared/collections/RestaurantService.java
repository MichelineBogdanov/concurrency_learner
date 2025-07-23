package course.concurrency.m3_shared.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RestaurantService {

    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private final Map<String, AtomicInteger> stat = new ConcurrentHashMap<>() {{
        put("A", new AtomicInteger(0));
        put("B", new AtomicInteger(0));
        put("C", new AtomicInteger(0));
    }};

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        stat.get(restaurantName).incrementAndGet();
        // your code

    }

    public Set<String> printStat() {
        // your code
        return stat.keySet().stream().map(s -> s + " - " + stat.get(s).get()).collect(Collectors.toSet());
    }
}
