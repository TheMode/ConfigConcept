package fr.themode.config;

public class Main {
    public static void main(String[] args) {
        Config config = Config.builder()
                .set(Config.Message.JOIN, "message")
                .set(Config.Misc.NUMBER, 5)
                .build();
        System.out.println("join: " + config.get(Config.Message.JOIN));

        var json = ConfigSerializers.toJson(config);
        System.out.println(json);

        System.out.println("equals " + config.equals(ConfigSerializers.fromJson(json)));
    }
}
