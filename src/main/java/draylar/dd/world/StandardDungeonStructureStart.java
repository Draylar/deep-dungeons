package draylar.dd.world;

import draylar.dd.api.*;
import draylar.dd.style.Style;
import draylar.dd.style.StyleRegistry;
import draylar.dd.world.api.SiftingStructureStart;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDungeonStructureStart extends SiftingStructureStart {

    public StandardDungeonStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
        super(feature, chunkX, chunkZ, box, references, seed);
    }

    @Override
    public Map<BlockPos, BlockInfo> place(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
        long start = System.currentTimeMillis();
        BlockPos origin = new BlockPos(chunkX * 16, 30, chunkZ * 16);
        Map<BlockPos, BlockInfo> blocks = new HashMap<>();
        List<Room> rooms = new ArrayList<>();
        int roomCount = 15 + random.nextInt(10);
        List<Connection> connections = new ArrayList<>();
        Style style = StyleRegistry.get(registryManager.get(Registry.BIOME_KEY).getKey(biome).get(), biome, origin);

        // Initialize starting rooms at 0, 0
        for (int i = 0; i < roomCount; i++) {
            int height = 6 + random.nextInt(3);
            rooms.add(new Room(10 + random.nextInt(10), 10 + random.nextInt(10), height, Vel2D.getRandom(random)));
        }

        // Spread out rooms
        rooms.forEach(room -> {
            while(room.intercepts(rooms)) {
                room.move();
            }
        });

        // Rooms -> blocks
        rooms.forEach(room -> blocks.putAll(room.build(origin, random, style)));

        // doors
        rooms.forEach(from -> {
            Room expanded = from.expand();

            rooms.stream().filter(room -> room != from).filter(expanded::intercepts).forEach(intersects -> {
                if(connections.stream().anyMatch(connection -> connection.contains(from, intersects))) {
                    return;
                }

                connections.add(new Connection(from, intersects));

                // Find the average middle position between the two rooms
                List<BlockPos> interceptionPoints = expanded.getInterceptionPoints(intersects);
                int totalX = 0;
                int totalZ = 0;

                for (BlockPos inter : interceptionPoints) {
                    totalX += inter.getX();
                    totalZ += inter.getZ();
                }

                // 3x3 at random intersection point
                BlockPos nOrigin = new BlockPos(totalX / interceptionPoints.size(), 0, totalZ / interceptionPoints.size());

                // place
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        for (int y = 1; y <= 4; y++) {
                            blocks.put(origin.add(nOrigin).add(x, y, z), new BlockInfo(Blocks.AIR.getDefaultState(), null));
                        }
                    }
                }
            });
        });

        // post-processing: chests and spawners
        blocks.forEach((pos, info) -> {

            // State is NOT air.
            if(!info.state.isAir()) {
                BlockPos up = pos.up();

                // If the position above this block exists...
                if(blocks.containsKey(up)) {
                    BlockInfo upState = blocks.get(up);

                    // If it is air, this is a valid placement position.
                    if(upState.state.isAir()) {
                        if (random.nextInt(150) == 0) {
                            blocks.put(up, new BlockInfo(Blocks.CHEST.getDefaultState(), FeatureHelper.createChestTag(new Identifier("chests/simple_dungeon"), up)));
                        } else if (random.nextInt(150) == 0) {
                            blocks.put(up, new BlockInfo(Blocks.SPAWNER.getDefaultState(), null));
                        }
                    }
                }
            }
        });

        System.out.println("Took: " + (System.currentTimeMillis() - start) + " to finish");

        return blocks;
    }
}
