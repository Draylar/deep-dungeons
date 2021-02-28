package draylar.dd;

import draylar.dd.registry.DeepDungeonFeatures;
import draylar.dd.registry.DeepDungeonPieces;
import draylar.dd.registry.DeepDungeonWorld;
import draylar.dd.style.Style;
import draylar.dd.style.StyleRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class DeepDungeons implements ModInitializer {

    @Override
    public void onInitialize() {
        DeepDungeonPieces.init();
        DeepDungeonFeatures.init();
        DeepDungeonWorld.init();

        // GENERAL DIMENSION FALLBACKS ---------------------------------------------------------------------------------------------------------------------
        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.THEEND), 0, new Style()
                .addWallBlock(Blocks.END_STONE_BRICKS)
                .addFloorBlock(Blocks.END_STONE).addFloorBlock(Blocks.END_STONE_BRICKS)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.NETHER), 0, new Style()
                .addWallBlock(Blocks.NETHER_BRICKS)
                .addFloorBlock(Blocks.NETHERRACK)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        // BIOME CATEGORIES ---------------------------------------------------------------------------------------------------------------------
        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.DESERT), 1, new Style()
                .addWallBlock(Blocks.SANDSTONE).addWallBlock(Blocks.SMOOTH_SANDSTONE)
                .addFloorBlock(Blocks.SANDSTONE).addFloorBlock(Blocks.SMOOTH_SANDSTONE).addFloorBlock(Blocks.SAND)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.ICY) || biome.getCategory().equals(Biome.Category.TAIGA), 1, new Style()
                .addWallBlock(Blocks.ICE).addWallBlock(Blocks.BLUE_ICE).addWallBlock(Blocks.PACKED_ICE)
                .addFloorBlock(Blocks.PACKED_ICE)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.MESA), 1, new Style()
                .addWallBlock(Blocks.ORANGE_TERRACOTTA).addWallBlock(Blocks.RED_TERRACOTTA)
                .addFloorBlock(Blocks.RED_TERRACOTTA)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.MUSHROOM), 1, new Style()
                .addWallBlock(Blocks.BROWN_MUSHROOM_BLOCK).addWallBlock(Blocks.RED_MUSHROOM_BLOCK)
                .addFloorBlock(Blocks.BROWN_MUSHROOM_BLOCK)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.OCEAN), 1, new Style()
                .addWallBlock(Blocks.PRISMARINE).addWallBlock(Blocks.PRISMARINE_BRICKS)
                .addFloorBlock(Blocks.PRISMARINE_BRICKS).addFloorBlock(Blocks.PRISMARINE)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));


        // SPECIFIC BIOMES ---------------------------------------------------------------------------------------------------------------------
        StyleRegistry.registerBiomeStyle((key, biome, originPosition) -> key.equals(BiomeKeys.BASALT_DELTAS), 2, new Style()
                .addWallBlock(Blocks.POLISHED_BLACKSTONE_BRICKS).addWallBlock(Blocks.POLISHED_BLACKSTONE_BRICKS)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));
    }

    public static Identifier id(String name) {
        return new Identifier("deepdungeons", name);
    }
}
