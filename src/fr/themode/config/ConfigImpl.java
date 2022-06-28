package fr.themode.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

record ConfigImpl(Map<Entry<?>, Object> entries) implements Config {
    static final EntryImpl<String> MESSAGE_JOIN = new EntryImpl<>(String.class, List.of("message"), "join");
    static final EntryImpl<Integer> MISC_NUMBER = new EntryImpl<>(int.class, List.of("misc"), "number");

    static final List<EntryImpl<?>> ENTRIES = List.of(MESSAGE_JOIN, MISC_NUMBER);

    static <T> EntryImpl<T> find(Class<T> type, List<String> path, String name) {
        for (EntryImpl<?> entry : ENTRIES) {
            if (type != null && type != entry.type())
                continue; // Incompatible type
            if (entry.path().equals(path) && entry.name().equals(name))
                return (EntryImpl<T>) entry;
        }
        return null;
    }

    public ConfigImpl {
        entries = Map.copyOf(entries);
    }

    @Override
    public <T> T get(Entry<T> entry) {
        return (T) entries.get(entry);
    }

    record EntryImpl<T>(Class<T> type, List<String> path, String name) implements Entry<T> {
        public EntryImpl {
            path = List.copyOf(path);
        }
    }

    static final class BuilderImpl implements Builder {
        Map<Entry<?>, Object> entries = new HashMap<>();

        @Override
        public <T> Builder set(Entry<T> entry, T value) {
            this.entries.put(entry, value);
            return this;
        }

        @Override
        public Config build() {
            return new ConfigImpl(entries);
        }
    }
}
