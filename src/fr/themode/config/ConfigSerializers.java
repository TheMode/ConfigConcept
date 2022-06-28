package fr.themode.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class ConfigSerializers {
    public static final Map<Class<?>, Function<Object, JsonPrimitive>> WRITE_MAP = Map.of(
            String.class, (o) -> new JsonPrimitive((String) o),
            int.class, (o) -> new JsonPrimitive((Number) o));

    public static JsonObject toJson(Config config) {
        final JsonObject json = new JsonObject();
        config.entries().forEach((entry, o) -> {
            JsonObject target = json;
            for (String path : entry.path()) {
                if (!(target.get(path) instanceof JsonObject object)) {
                    JsonObject tmp = new JsonObject();
                    target.add(path, tmp);
                    target = tmp;
                } else {
                    target = object;
                }
            }
            var primitive = WRITE_MAP.get(entry.type()).apply(o);
            target.add(entry.name(), primitive);
        });
        return json;
    }

    public static Config fromJson(JsonObject json) {
        Config.Builder builder = Config.builder();
        json.entrySet().forEach(entry -> {
            JsonElement value = entry.getValue();
            if (value instanceof JsonObject object) {
                append(builder, object, List.of(entry.getKey()));
            }
        });
        return builder.build();
    }

    private static void append(Config.Builder builder, JsonObject json, List<String> path) {
        json.entrySet().forEach(entry -> {
            final String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value instanceof JsonObject object) {
                List<String> childPath = new ArrayList<>(path);
                childPath.add(entry.getKey());
                append(builder, object, childPath);
            } else {
                final Config.Entry test = Config.Entry.find(path, key);
                if (test.type() == String.class) {
                    builder.set(test, value.getAsString());
                } else if (test.type() == int.class) {
                    builder.set(test, value.getAsInt());
                } else {
                    throw new IllegalStateException("invalid type " + test + " " + value);
                }
            }
        });
    }
}
