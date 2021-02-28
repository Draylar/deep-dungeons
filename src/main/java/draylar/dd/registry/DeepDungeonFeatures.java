package draylar.dd.registry;

import draylar.dd.DeepDungeons;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;

public class DeepDungeonFeatures {

    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_ISLAND = register("standard", DeepDungeonWorld.STANDARD.configure(DefaultFeatureConfig.INSTANCE));

    private static <FC extends FeatureConfig, F extends StructureFeature<FC>> ConfiguredStructureFeature<FC, F> register(String id, ConfiguredStructureFeature<FC, F> configuredStructureFeature) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, DeepDungeons.id(id), configuredStructureFeature);
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, DeepDungeons.id(id), configuredFeature);
    }

    public static void init() {

    }

    private DeepDungeonFeatures() {

    }
}
