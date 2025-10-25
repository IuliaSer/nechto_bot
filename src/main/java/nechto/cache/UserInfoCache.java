package nechto.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserInfoCache {
    private final Map<Long, AtomicInteger> map = new ConcurrentHashMap<>();

    public void put(long userId) {
        map.putIfAbsent(userId, new AtomicInteger());
    }

    public AtomicInteger get(long userId) {
        return map.get(userId);
    }
}
