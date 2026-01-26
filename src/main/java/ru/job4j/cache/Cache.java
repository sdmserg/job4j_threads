package ru.job4j.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final ConcurrentHashMap<Integer, Base> cache = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        if (model == null) {
            return false;
        }
        return cache.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        if (model == null) {
            return false;
        }
        var result = cache.computeIfPresent(model.id(), (id, base) -> {
            if (cache.get(id).version() != model.version()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(id, model.name(), base.version() + 1);
        });
        return result != null;
    }

    public void delete(int id) {
        cache.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }
}
