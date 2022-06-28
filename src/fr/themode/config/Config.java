package fr.themode.config;

import java.util.List;
import java.util.Map;

public interface Config {

    <T> T get(Entry<T> entry);

    Map<Entry<?>, Object> entries();

    static Builder builder() {
        return new ConfigImpl.BuilderImpl();
    }

    interface Message {
        Entry<String> JOIN = ConfigImpl.MESSAGE_JOIN;
    }

    interface Misc {
        Entry<Integer> NUMBER = ConfigImpl.MISC_NUMBER;
    }

    interface Builder {
        <T> Builder set(Entry<T> entry, T value);

        Config build();
    }

    interface Entry<T> {
        Class<T> type();

        List<String> path();

        String name();

        static <T> Entry<T> find(Class<T> type, List<String> path, String name) {
            return ConfigImpl.find(type, path, name);
        }

        static Entry<?> find(List<String> path, String name) {
            return find(null, path, name);
        }
    }
}
