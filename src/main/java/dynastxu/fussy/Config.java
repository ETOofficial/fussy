package dynastxu.fussy;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

//    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
//            .comment("Whether to log the dirt block on common setup")
//            .define("logDirtBlock", true);
//
//    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
//            .comment("A magic number")
//            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
//
//    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
//            .comment("What you want the introduction message to be for the magic number")
//            .define("magicNumberIntroduction", "The magic number is... ");
//
//    // a list of strings that are treated as resource locations for items
//    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
//            .comment("A list of items to log on common setup.")
//            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

//    private static boolean validateItemName(final Object obj) {
//        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
//    }

    public static final ModConfigSpec.BooleanValue RESET_ON_DEATH = BUILDER
            .comment("resetOnDeath")
            .define("resetOnDeath", false);

    public static final ModConfigSpec.IntValue PREFERENCE_REDUCE_PERCENT = BUILDER
            .comment("preferenceReducePercent")
            .defineInRange("preferenceReducePercent", 1, 0, 100);

    public static final ModConfigSpec.IntValue DEFAULT_PREFERENCE_PERCENT = BUILDER
            .comment("defaultPreferencePercent")
            .defineInRange("defaultPreferencePercent", 100, 0, 100);

    public static final ModConfigSpec.DoubleValue PREFERENCE_RAISE_AMOUNT = BUILDER
            .comment("preferenceRaiseAmount")
            .defineInRange("preferenceRaiseAmount", 0.0001d, 0d, 1d);

    public static final ModConfigSpec.IntValue SPIT_UP_THRESHOLD = BUILDER
            .comment("spitUpThreshold")
            .defineInRange("spitUpThreshold", 30, 0, 100);

    public static final ModConfigSpec.IntValue HATE_THRESHOLD = BUILDER
            .comment("hateThreshold")
            .defineInRange("hateThreshold", 60, 0, 100);

    static final ModConfigSpec SPEC = BUILDER.build();
}
