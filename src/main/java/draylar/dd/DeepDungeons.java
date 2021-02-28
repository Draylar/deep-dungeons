package draylar.dd;

import draylar.dd.config.DeepDungeonsConfig;
import draylar.dd.registry.DeepDungeonFeatures;
import draylar.dd.registry.DeepDungeonPieces;
import draylar.dd.registry.DeepDungeonWorld;
import draylar.dd.style.DungeonType;
import draylar.dd.style.SimpleDungeonRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class DeepDungeons implements ModInitializer {

    public static final DeepDungeonsConfig CONFIG = AutoConfig.register(DeepDungeonsConfig.class, GsonConfigSerializer::new).getConfig();

    @Override
    public void onInitialize() {
        DeepDungeonPieces.init();
        DeepDungeonFeatures.init();
        DeepDungeonWorld.init();

        // GENERAL DIMENSION FALLBACKS ---------------------------------------------------------------------------------------------------------------------
        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.THEEND), 0, new DungeonType()
                .addWallBlock(Blocks.END_STONE_BRICKS)
                .addFloorBlock(Blocks.END_STONE).addFloorBlock(Blocks.END_STONE_BRICKS)
                .addLootTable(new Identifier("chests/end_city_treasure"), 1.0));

        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.NETHER), 0, new DungeonType()
                .addWallBlock(Blocks.NETHER_BRICKS)
                .addFloorBlock(Blocks.NETHERRACK)
                .addLootTable(new Identifier("chests/nether_bridge"), 1.0));

        // BIOME CATEGORIES ---------------------------------------------------------------------------------------------------------------------
        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.DESERT), 1, new DungeonType()
                .addWallBlock(Blocks.SANDSTONE).addWallBlock(Blocks.SMOOTH_SANDSTONE)
                .addFloorBlock(Blocks.SANDSTONE).addFloorBlock(Blocks.SMOOTH_SANDSTONE).addFloorBlock(Blocks.SAND)
                .addLootTable(new Identifier("chests/desert_pyramid"), 1.0));

        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.ICY) || biome.getCategory().equals(Biome.Category.TAIGA), 1, new DungeonType()
                .addWallBlock(Blocks.ICE).addWallBlock(Blocks.BLUE_ICE).addWallBlock(Blocks.PACKED_ICE)
                .addFloorBlock(Blocks.PACKED_ICE)
                .addLootTable(new Identifier("chests/simple_dungeon"), 1.0));

        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.MESA), 1, new DungeonType()
                .addWallBlock(Blocks.ORANGE_TERRACOTTA).addWallBlock(Blocks.RED_TERRACOTTA)
                .addFloorBlock(Blocks.RED_TERRACOTTA)
                .addLootTable(new Identifier("chests/abandoned_mineshaft"), 1.0));

        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.MUSHROOM), 1, new DungeonType()
                .addWallBlock(Blocks.BROWN_MUSHROOM_BLOCK).addWallBlock(Blocks.RED_MUSHROOM_BLOCK)
                .addFloorBlock(Blocks.BROWN_MUSHROOM_BLOCK)
                .addLootTable(new Identifier("chests/abandoned_mineshaft"), 1.0));

        SimpleDungeonRegistry.register((key, biome, originPosition) -> biome.getCategory().equals(Biome.Category.OCEAN), 1, new DungeonType()
                .addWallBlock(Blocks.PRISMARINE).addWallBlock(Blocks.PRISMARINE_BRICKS)
                .addFloorBlock(Blocks.PRISMARINE_BRICKS).addFloorBlock(Blocks.PRISMARINE)
                .addLootTable(new Identifier("chests/underwater_ruin_big"), 1.0));


        // SPECIFIC BIOMES ---------------------------------------------------------------------------------------------------------------------
        SimpleDungeonRegistry.register((key, biome, originPosition) -> key.equals(BiomeKeys.BASALT_DELTAS), 2, new DungeonType()
                .addWallBlock(Blocks.POLISHED_BLACKSTONE_BRICKS).addWallBlock(Blocks.POLISHED_BLACKSTONE_BRICKS)
                .addLootTable(new Identifier("chests/bastion_other"), 0.5)
                .addLootTable(new Identifier("chests/bastion_bridge"), 1.0)
                .addLootTable(new Identifier("chests/bastion_treasure"), 0.1));
    }

    public static Identifier id(String name) {
        return new Identifier("deepdungeons", name);
    }
}
