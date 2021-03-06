package draylar.dd.registry;

import draylar.dd.DeepDungeons;
import draylar.dd.world.StandardDungeonStructure;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class DeepDungeonWorld {

    public static final StructureFeature<DefaultFeatureConfig> STANDARD_DUNGEON = new StandardDungeonStructure(DefaultFeatureConfig.CODEC);

    public static void init() {
        registerStructures();
        registerAdditions();
    }

    public static void registerStructures() {
        FabricStructureBuilder
                .create(DeepDungeons.id("standard_dungeon"), STANDARD_DUNGEON)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(DeepDungeons.CONFIG.standardDungeonSpacing, DeepDungeons.CONFIG.standardDungeonSeparation, 62326)
                .register();
    }

    public static void registerAdditions() {
        BiomeModifications
                .create(DeepDungeons.id("standard_dungeon"))
                .add(ModificationPhase.ADDITIONS,
                        context -> !DeepDungeons.CONFIG.standardDungeonBiomeBlacklist.contains(context.getBiomeKey().getValue().toString()),
                        context -> context.getGenerationSettings().addBuiltInStructure(DeepDungeonFeatures.CONFIGURED_STANDARD_DUNGEON));
    }

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registry.FEATURE, DeepDungeons.id(name), feature);
    }

    private DeepDungeonWorld() {
        // no-op
    }
}
